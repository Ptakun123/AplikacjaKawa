import os
from flask import Flask, request, jsonify
from flask_jwt_extended import JWTManager, create_access_token, jwt_required, get_jwt_identity
from models import db, User
from argon2 import PasswordHasher
from argon2.exceptions import VerifyMismatchError

app = Flask(__name__)

# Konfiguracja z bezpiecznych zmiennych środowiskowych
app.config['SQLALCHEMY_DATABASE_URI'] = os.getenv('DATABASE_URL')
app.config['SQLALCHEMY_TRACK_MODIFICATIONS'] = False
app.config['JWT_SECRET_KEY'] = os.getenv('JWT_SECRET_KEY')
app.config['JWT_ACCESS_TOKEN_EXPIRES'] = 900 # 15 minut dla bezpieczeństwa

db.init_app(app)
jwt = JWTManager(app)
ph = PasswordHasher()

with app.app_context():
    db.create_all() # W produkcji użyj Flask-Migrate (Alembic) zamiast create_all()

@app.route('/api/auth/register', methods=['POST'])
def register():
    data = request.get_json()
    if not data or not data.get('username') or not data.get('password'):
        return jsonify({"error": "Missing username or password"}), 400

    if User.query.filter_by(username=data['username']).first():
        return jsonify({"error": "Username already exists"}), 409

    hashed_password = ph.hash(data['password'])
    new_user = User(username=data['username'], password_hash=hashed_password)
    
    db.session.add(new_user)
    db.session.commit()
    
    return jsonify({"message": "User created successfully"}), 201

@app.route('/api/auth/login', methods=['POST'])
def login():
    data = request.get_json()
    if not data or not data.get('username') or not data.get('password'):
        return jsonify({"error": "Missing credentials"}), 400

    user = User.query.filter_by(username=data['username']).first()
    if not user:
        return jsonify({"error": "Invalid credentials"}), 401

    try:
        ph.verify(user.password_hash, data['password'])
    except VerifyMismatchError:
        return jsonify({"error": "Invalid credentials"}), 401

    # Aktualizacja hasha, jeśli parametry Argon2 uległy zmianie/zaostrzeniu w kodzie
    if ph.check_needs_rehash(user.password_hash):
        user.password_hash = ph.hash(data['password'])
        db.session.commit()

    access_token = create_access_token(identity=user.id)
    return jsonify(access_token=access_token), 200

@app.route('/api/secure-data', methods=['GET'])
@jwt_required()
def secure_data():
    current_user_id = get_jwt_identity()
    return jsonify({"data": "Confidential info", "user_id": current_user_id}), 200

# Zapobieganie wyciekom informacji o stacku w nagłówkach
@app.after_request
def remove_server_header(response):
    response.headers['Server'] = 'Hidden'
    return response