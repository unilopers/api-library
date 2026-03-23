import time

from locust import HttpUser, task, between


class LoadTestUser(HttpUser):
    # Reduz o intervalo entre requisições para aumentar a carga no endpoint.
    wait_time = between(0.1, 0.4)

    def on_start(self):
        unique = int(time.time() * 1000)

        self.email = f"johndoe{unique}@example.com"
        self.password = "password123"
        self.auth_headers = {}

        self.client.post(
            "/client",
            json={
                "name": f"John Doe {unique}",
                "phone": f"11999{unique % 100000:05d}",
                "email": self.email,
                "password": self.password,
            },
            name="POST /client",
        )

        login_response = self.client.post(
            "/auth/login",
            json={"email": self.email, "password": self.password},
            name="POST /auth/login",
        )

        if login_response.status_code == 200:
            token = login_response.json().get("token")
            if token:
                self.auth_headers = {"Authorization": f"Bearer {token}"}

    @task(10)
    def get_all_clients(self):
        self.client.get(
            "/client",
            headers=self.auth_headers,
            name="GET /client",
        )
