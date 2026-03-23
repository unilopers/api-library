import time

from locust import HttpUser, task, between

class LoadTestUser(HttpUser):

    @task
    def test_create_user(self):
        unique = int(time.time() * 1000)
        email = f"johndoe{unique}@example.com"
        password = "password123"        
        
        self.client.post(
            "/client",
            json={
                "name": f"John Doe {unique}",
                "phone": f"11999{unique % 100000:05d}",
                "email": email,
                "password": password
            }
        )

        self.client.post(
            "/auth/login",
            json={
                "email": email,
                "password": password
            }
        )
