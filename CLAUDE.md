# Simulador de Transacciones

Monolito en Spring Boot que simula autorizaciones bancarias. Tiene dos APIs internas:
- API 1 (pública): recibe la transacción del cliente, valida, sanitiza y la procesa.
- API 2 (interna): simula el autorizador bancario. Solo accesible desde API 1 mediante un header secreto.

Stack: Java 21, Spring Boot 3.5.15, Spring Security, JWT, Hibernate/JPA, H2 en memoria.

## Arquitectura

Arquitectura en capas. El flujo de una petición es siempre:

controller → service (interface) → serviceImpl → repository → model

### Convenciones por capa
- **model/**: entidades JPA. Una clase = una tabla. Usan Lombok (@Data, @NoArgsConstructor, @AllArgsConstructor).
- **repository/**: SIEMPRE interfaces que extienden JpaRepository. Nunca clases.
- **service/**: cada servicio es un par interface + Impl. La lógica de negocio vive aquí, nunca en el controller.
- **controller/**: solo recibe el request, aplica @Valid y delega al service. No lleva lógica.
- **dto/**: define qué entra y qué sale. Las validaciones (@NotBlank, @Pattern, etc.) van aquí.
- **config/**: seguridad, JWT, CORS, Swagger, manejo de excepciones.

### Reglas que no se rompen
- La validación de formato va en el DTO con anotaciones, nunca en el service.
- La sanitización (OWASP) va en el service, antes de procesar.
- API 2 (/api/autorizacion) solo se llama internamente con el header X-Internal-Secret. Nunca se expone al cliente.
- Los endpoints de transacciones requieren JWT. Las rutas públicas son solo /api/auth/**.

### Comentarios
- Cada método lleva un comentario arriba explicando qué hace y por qué existe, en español.
- Dentro del método, comentar solo los pasos no evidentes (el porqué de una decisión), no los obvios.
- El comentario explica el porqué, no repite el qué. Si el código ya lo dice claro, no se comenta.
- Como este proyecto es de aprendizaje, los comentarios pueden ser un poco más explicativos de lo normal.

## Comandos

Levantar el backend (desde la raíz del proyecto):

./mvnw clean spring-boot:run

- Backend: http://localhost:8080
- Swagger: http://localhost:8080/swagger-ui/index.html
- Consola H2: http://localhost:8080/h2-console

Levantar el frontend (React + Vite, desde su carpeta):

npm run dev

- Frontend: http://localhost:5174

## Cuidados

- H2 es en memoria: al reiniciar el backend se borra todo, incluidos los usuarios. Hay que registrarse de nuevo.
- Las credenciales (contraseña de BD, secreto JWT, secreto interno) están en application.properties en texto plano. Es aceptable SOLO porque es un proyecto de aprendizaje. En producción irían en un gestor de secretos.
- El token JWT expira en 15 minutos. Si una petición protegida da 401, probablemente expiró.
- No exponer /api/autorizacion al cliente bajo ninguna circunstancia.