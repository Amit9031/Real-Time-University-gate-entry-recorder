## University Gate Entry – QR + DevOps Demo

**Tech stack:** Java 17, Spring Boot, Maven, Thymeleaf, Docker, Jenkins, Kubernetes  
**Scenario:** A student scans a QR at the university gate on their phone → phone shows **YES / NO** → on **YES** the entry is recorded and shown on the **laptop/security dashboard**.

### How the application works

- **Phone side (QR simulation)**
  - QR code content is a URL like `http://localhost:8080/qr/1` (1, 2, 3 are sample students).
  - Opening that URL on a phone browser shows a **confirmation screen** with:
    - Student name
    - Registration number
    - Phone number
  - Student taps **YES** or **NO**:
    - **YES** → entry is stored in memory and a success screen is shown.
    - **NO** → entry is not stored and a cancelled screen is shown.

- **Laptop side (security view)**
  - Open `http://localhost:8080/` on a laptop/desktop.
  - Shows a **live entry table** with:
    - Student name
    - Registration number
    - Phone number
    - Entry time
  - Page auto-refreshes every 5 seconds so it feels real-time.

### Important endpoints

- `GET /` – laptop **security dashboard** (Thymeleaf template `laptop.html`).
- `GET /qr/{studentId}` – **QR scan landing page** for the phone (template `phone-confirm.html`).
- `POST /confirm-entry` – handles **YES / NO** from phone and returns:
  - `phone-success.html` on YES
  - `phone-denied.html` on NO

Sample students are pre-loaded in `StudentRepository` (IDs 1, 2, 3).

---

### How to run locally (Dev → Maven)

1. Make sure you have **JDK 17+** and **Maven** installed.
2. From project root:

```bash
mvn spring-boot:run
```

3. Open:
   - Laptop view: `http://localhost:8080/`
   - Phone view / QR simulation: `http://localhost:8080/qr/1`

---

### Docker (Maven → Docker)

Build the jar and Docker image:

```bash
mvn -DskipTests clean package
docker build -t university/gate-entry:latest .
```

Run the container:

```bash
docker run -p 8080:8080 university/gate-entry:latest
```

Then access as before:
- `http://localhost:8080/` (laptop)
- `http://localhost:8080/qr/1` (phone/QR)

---

### Jenkins pipeline (Git → Maven → Jenkins → Docker → Kubernetes)

This repo contains a `Jenkinsfile` with stages:

1. **Checkout** – pulls code from Git.
2. **Build with Maven** – `mvn -DskipTests clean package`.
3. **Build Docker Image** – `docker build -t university/gate-entry:latest .`.
4. **Push Docker Image** (optional) – if `DOCKER_REGISTRY_CREDENTIALS` is configured.
5. **Deploy to Kubernetes** – applies `k8s/deployment.yaml` and `k8s/service.yaml`.

On Jenkins:
- Configure **Maven** tool named `Maven-3`.
- Configure **JDK** tool named `JDK-17`.
- Optionally, configure Docker registry credentials and set `DOCKER_REGISTRY_CREDENTIALS` in pipeline env.

---

### Kubernetes (Docker → Kubernetes)

Kubernetes manifests are in the `k8s/` directory:

- `deployment.yaml` – runs the Spring Boot container (`university/gate-entry:latest`).
- `service.yaml` – exposes the app as a `NodePort` on `30080`.

Apply them:

```bash
kubectl apply -f k8s/deployment.yaml
kubectl apply -f k8s/service.yaml
```

Then access:

- `http://<node-ip>:30080/` – laptop view.
- `http://<node-ip>:30080/qr/1` – phone / QR simulation.

This end‑to‑end flow demonstrates **Git → Maven → Jenkins → Docker → Kubernetes** for your DevOps project with a clear, real-time university gate entry use case.


