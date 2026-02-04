# Manual de Proyecto: MemoryAid

---

## 1. Introducción y Propósito
MemoryAid es una plataforma de soporte cognitivo diseñada para mitigar la carga operativa de los cuidadores. El sistema centraliza la monitorización de eventos críticos (crisis, tomas de medicación) y proporciona herramientas de análisis de datos para optimizar el seguimiento clínico del paciente.

---

## 2. Manual de Usuario (Guía Operativa)

### 2.1 Flujo de Configuración Inicial (Onboarding)
Al ejecutar la aplicación por primera vez, el sistema detectará la ausencia de credenciales:
1. **Acceso Admin:** Pulse el icono de configuración en la barra superior.
2. **Establecer PIN:** El sistema le obligará a definir un código de 4 dígitos. Este PIN es vital para proteger la privacidad de los datos clínicos y la configuración de emergencia.
3. **Contacto de Auxilio:** Configure el número de teléfono del cuidador principal o servicio médico. Sin este paso, la función SOS permanecerá inactiva.

### 2.2 Gestión de Eventos Diarios
El núcleo de la aplicación es el registro de actividad mediante un flujo simplificado:
* **Categorización:** Cada registro debe clasificarse (Medicina, Crisis, Actividad, Alimentación) para que el motor de estadísticas pueda procesar los datos correctamente.
* **Entrada de Datos NUI:** Utilice el botón de micrófono en el formulario para realizar anotaciones mediante dictado. La aplicación procesa el lenguaje natural y lo vuelca en el campo de descripción.
* **Persistencia de Alertas:** Si activa un recordatorio para una medicina, la aplicación programará una notificación en el sistema. Estas alarmas son resistentes a reinicios del terminal.

### 2.3 Panel de Control y Análisis
El área de administración proporciona una visión cuantitativa:
* **Métricas Semanales:** Visualización de la tendencia de crisis y adherencia a la medicación.
* **Generación de Informes:** Pulse "Generar Informe" para consolidar toda la base de datos en un formato de texto estructurado listo para ser compartido con médicos vía Email o WhatsApp.
* **Zona de Peligro:** Opción para purgar la base de datos. Se requiere confirmación doble debido a la irreversibilidad de la acción.

---

## 3. Manual Técnico (Arquitectura y Desarrollo)

### 3.1 Arquitectura del Software
MemoryAid se ha desarrollado bajo los principios de **Clean Architecture**, asegurando que la lógica de negocio sea independiente de la interfaz de usuario y de la base de datos.

* **Capa de Presentación (UI/ViewModel):** Implementada al 100% en **Jetpack Compose**. Utiliza el patrón **Unidirectional Data Flow (UDF)**, donde el ViewModel expone un único estado (`UiState`) y recibe eventos de la vista.
* **Capa de Dominio (Use Cases):** Contiene la lógica pura. Cada acción (ej. `EliminarEventoUseCase`) es una unidad de lógica testeable e independiente.
* **Capa de Datos (Data/Repository):** Gestiona la fuente de verdad. Implementa el patrón Repository para decidir si los datos provienen de la base de datos local o de las preferencias del sistema.



### 3.2 Persistencia y Gestión de Estados
1. **Room Database:** Almacena los eventos de memoria. Se utiliza `Flow<List<Evento>>` para que cualquier cambio en la base de datos se refleje instantáneamente en la interfaz de usuario sin necesidad de recargar.
2. **DataStore (Preferences):** Almacena el PIN de administrador y el número de emergencia de forma reactiva. A diferencia de SharedPreferences, DataStore es seguro para transacciones en el hilo principal mediante Corrutinas.
3. **WorkManager:** Encargado de la lógica de notificaciones fuera de línea. Asegura que los recordatorios se disparen exactamente en el tiempo programado utilizando el `AlarmManager` del sistema.



### 3.3 Stack de Librerías Críticas
* **Hilt:** Inyección de dependencias para desacoplar componentes y facilitar tests unitarios.
* **Retrofit (Opcional/Preparado):** Estructura lista para integración con APIs REST de telemedicina.
* **Coil:** Gestión eficiente de carga de imágenes en memoria.
* **Material 3:** Sistema de diseño moderno con soporte para colores dinámicos.

---

## 4. Guía de Instalación para Evaluadores
Para desplegar el entorno de desarrollo:
1. Clone el repositorio: `git clone [https://github.com/JoseLuis-S/MemoryAid.git]`.
2. Abra el proyecto en **Android Studio Ladybug** o superior.
3. Sincronice Gradle para descargar las dependencias.
4. Ejecute la variante `debug` en un emulador con API 31+ o dispositivo físico.

**Permisos Requeridos:**
* `CALL_PHONE`: Para la función de SOS.
* `POST_NOTIFICATIONS`: Para los recordatorios de medicación (Android 13+).
* `RECORD_AUDIO`: Para el dictado de notas por voz.

