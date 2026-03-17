from locust import HttpUser, task, between

class LoadTestUser(HttpUser):

    def on_start(self):
        # This method is called when a simulated user starts
        self.client.post("/auth/login", json={"email": "talhettialvaro@gmail.com", "password": "123456789"})