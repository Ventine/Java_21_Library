# Java 21 Codebase

Repositorio orientado a consolidar implementaciones precisas y modernas usando Java 21. El objetivo es documentar patrones, técnicas y módulos desarrollados con enfoque productivo, seguro y mantenible.

## Alcance
- Ejemplos completos basados en APIs del JDK 21.
- Uso de records, sealed types, pattern matching y virtual threads.
- Integración con APIs externas mediante `HttpClient`.
- Utilidades de concurrencia y diseño orientado a inmutabilidad.
- Prácticas de seguridad y saneamiento de datos.
- Módulos aislados listos para prueba.

## Estructura
- `/core` componentes independientes y reutilizables.
- `/api` clientes HTTP, validaciones y manejo de fallos.
- `/examples` demostraciones funcionales.
- `/benchmarks` mediciones de rendimiento específicas.

## Objetivo del repositorio
Reunir fragmentos de código alineados con estándares actuales de la plataforma Java, priorizando claridad, precisión y robustez. Cada archivo se incluirá con propósito específico y sin dependencias innecesarias.

## Requisitos
- JDK 21
- Maven o Gradle (cualquiera compatible)

## Ejecución rápida
Compilación:
```bash
mvn clean package
