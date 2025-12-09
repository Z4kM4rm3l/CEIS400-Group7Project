# Identity Service (JDK 17)

## ✅ Run in GitHub Codespaces (No Local Setup)

You can run this project directly in the cloud using **GitHub Codespaces**:

### Steps:
1. Go to the GitHub repository for this project.
2. Click the green **Code** button, then select **Open with Codespaces** → **New codespace**.
3. Wait for the Codespace to initialize (Java and Maven are pre-installed).
4. In the Codespaces terminal, navigate to the `identity_service` module:
   ```bash
   cd identity_service
   ```
5. Run the application:
   ```bash
   mvn spring-boot:run
   ```
6. Once the app starts, Codespaces will show a **forwarded port link**. Click it to open the app in your browser.
7. Access Swagger UI at:
   ```
   https://<forwarded-port-url>/swagger-ui.html
   ```

### Why Codespaces?
- No need to install Java or Maven locally.
- Everything runs in the browser.

## ✅ Notes for Professor
- Pre-seeded users :
    - `employee@toolvault.local` / `Emp123!` (ROLE_USER)
    - `manager@toolvault.local` / `Mgr123!` (ROLE_ADMIN)

---

## ✅ Profiles
- Default profile: `dev`
- Test profile: `test` (used for automated tests)

Enjoy testing! 🎉

