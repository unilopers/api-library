import time
from uuid import uuid4

from locust import HttpUser, between, task

class LoadTestUser(HttpUser):
    host = "http://localhost:8080"
    wait_time = between(0.1, 0.4)

    def on_start(self):
        unique = f"{int(time.time() * 1000)}-{uuid4().hex[:6]}"
        email = f"johndoe{unique}@example.com"
        password = "password123"

        self.client.post(
            "/client",
            json={
                "name": f"John Doe {unique}",
                "phone": f"11999{int(time.time() * 1000) % 100000:05d}",
                "email": email,
                "password": password,
            },
            name="POST /client",
        )

        login_response = self.client.post(
            "/auth/login",
            json={"email": email, "password": password},
            name="POST /auth/login",
        )

        self.auth_headers = {}
        if login_response.status_code == 200:
            token = login_response.json().get("token")
            if token:
                self.auth_headers = {"Authorization": f"Bearer {token}"}

        # Seed para evitar GET em lista vazia de livros.
        self.client.post(
            "/book/create",
            headers=self.auth_headers,
            json={
                "title": f"Seed Book {unique}",
                "author": "Load Test",
                "year": 2024,
                "available": True,
            },
            name="POST /book/create",
        )
    
    @task(3)
    def test_book_create_overload(self):
        unique = f"{int(time.time() * 1000)}-{uuid4().hex[:6]}"

        self.client.post(
            "/book/create",
            headers=self.auth_headers,
            json={
                "title": f"Book {unique}",
                "author": "Load Test",
                "year": 2025,
                "available": True,
            },
            name="POST /book/create",
        )

    @task(1)
    def test_read_records(self):
        self.client.get(
            "/book",
            headers=self.auth_headers,
            name="GET /book",
        )