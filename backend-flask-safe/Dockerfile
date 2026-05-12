FROM python:3.11-slim

ENV PYTHONDONTWRITEBYTECODE=1 \
    PYTHONUNBUFFERED=1

WORKDIR /backend-flask-safe

# Utworzenie użytkownika non-root
RUN addgroup --system appgroup && adduser --system --group appuser

# Instalacja zależności systemowych (potrzebne dla psycopg2 i argon2)
RUN apt-get update && apt-get install -y --no-install-recommends \
    gcc \
    libpq-dev \
    && rm -rf /var/lib/apt/lists/*

COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt

COPY . .

# Zmiana właściciela plików i przełączenie na non-root
RUN chown -R appuser:appgroup /backend-flask-safe
USER appuser

EXPOSE 5000

# Gunicorn w roli serwera produkcyjnego, usuwamy identyfikację serwera
CMD ["gunicorn", "--bind", "0.0.0.0:5000", "--workers", "4", "--worker-class", "gthread", "app:app"]