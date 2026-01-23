# Justificación de Criterios de Evaluación - MemoryAid

Este documento detalla el cumplimiento de los Resultados de Aprendizaje (RA) exigidos en la rúbrica oficial, vinculando las decisiones de diseño y arquitectura con la implementación técnica en el código fuente de MemoryAid.

---

## Índice de Criterios

### 1. Desarrollo de Interfaces (RA1)
* **RA1.a** Analiza herramientas y librerías
* **RA1.b** Crea interfaz gráfica
* **RA1.c** Uso de layouts y posicionamiento
* **RA1.d** Personalización de componentes
* **RA1.e** Análisis del código
* **RA1.f** Modificación del código
* **RA1.g** Asociación de eventos
* **RA1.h** App integrada

### 2. Interfaces Naturales de Usuario - NUI (RA2)
* **RA2.a** Herramientas NUI
* **RA2.b** Diseño conceptual NUI
* **RA2.c** Interacción por voz
* **RA2.d** Interacción por gesto
* **RA2.e** Detección facial/corporal
* **RA2.f** Realidad aumentada

### 3. Componentes de Software Reutilizables (RA3)
* **RA3.a** Herramientas de componentes
* **RA3.b** Componentes reutilizables
* **RA3.c** Parámetros y defaults
* **RA3.d** Eventos en componentes
* **RA3.f** Documentación
* **RA3.h** Integración en la app

### 4. Usabilidad y Estándares (RA4)
* **RA4.a** Estándares
* **RA4.b** Valoración de estándares
* **RA4.c** Menús
* **RA4.d** Distribución de acciones
* **RA4.e** Distribución de controles
* **RA4.f** Elección de controles
* **RA4.g** Diseño visual
* **RA4.h** Claridad de mensajes
* **RA4.i** Pruebas usabilidad
* **RA4.j** Evaluación en dispositivos

### 5. Generación de Informes (RA5)
* **RA5.a** Establece la estructura del informe
* **RA5.b** Genera informes a partir de fuentes de datos
* **RA5.c** Establece filtros sobre los valores a presentar
* **RA5.d** Incluye valores calculados, recuentos o totales
* **RA5.e** Incluye gráficos generados a partir de los datos

### 6. Ayuda y Documentación (RA6)
* **RA6.a** Identifica sistemas de generación de ayudas
* **RA6.b** Genera ayudas en formatos habituales
* **RA6.c** Genera ayudas sensibles al contexto
* **RA6.d** Documentación de la estructura de la información persistente
* **RA6.e** Manual de usuario y guía de referencia
* **RA6.f** Manual técnico de instalación/configuración
* **RA6.g** Confecciona tutoriales

### 7. Pruebas y Calidad (RA8)
* **RA8.a** Estrategia de pruebas
* **RA8.b** Pruebas de integración
* **RA8.g** Documentación pruebas

---

## 1. Desarrollo de Interfaces (RA1)

### RA1.a Analiza herramientas y librerías
**Justificación:** Se ha realizado una selección técnica fundamentada en el ecosistema moderno de Android. Se utiliza **Kotlin** por su seguridad nula intrínseca y **Jetpack Compose** para el desarrollo de una UI declarativa, lo que reduce el acoplamiento y el código boilerplate de los antiguos archivos XML.
* **Persistencia:** Room Database para abstracción de SQLite.
* **Inyección de Dependencias:** Hilt/Dagger para facilitar el testing y la modularización.
* **Arquitectura:** Clean Architecture con patrón MVVM para separar responsabilidades.

### RA1.b Crea interfaz gráfica
**Justificación:** La interfaz es profesional y sigue las guías de **Material Design 3**. Se ha estructurado mediante un sistema de estados (`HomeUiState`, `AdminUiState`) que garantiza una representación coherente de la lógica de negocio en la vista. La navegación es fluida gracias a `Navigation Compose`.
* **Implementación:** [Ver HomeScreen.kt](PON_AQUÍ_TU_PERMALINK)

### RA1.c Uso de layouts y posicionamiento
**Justificación:** Se utiliza una jerarquía visual avanzada. Se emplea `Scaffold` para la estructura básica (TopBars, FABs), `LazyColumn` para la gestión eficiente de listas con scroll y componentes de organización como `Box`, `Row` y `Column` con modificadores de peso (`weight`) para asegurar la adaptabilidad a diferentes tamaños de pantalla.

### RA1.d Personalización de componentes
**Justificación:** No se utilizan únicamente componentes nativos; se han personalizado elementos para mejorar la experiencia del cuidador:
* **Cards personalizadas:** Uso de elevaciones, bordes redondeados y esquemas de color específicos para categorías (Medicinas vs Crisis).
* **Diálogos:** Implementación de `AlertDialog` con estilos coherentes a la marca MemoryAid.
* **Animaciones:** Integración de Lottie para feedback visual en el guardado de datos.

### RA1.e Análisis del código
**Justificación:** El código implementa **Unidirectional Data Flow (UDF)**. El estado fluye desde el ViewModel hacia la UI, y los eventos fluyen desde la UI hacia el ViewModel mediante lambdas. Se evita el acceso directo a la base de datos desde la vista, delegando en casos de uso y repositorios.

### RA1.f Modificación del código
**Justificación:** Se ha realizado una refactorización crítica en la lógica de seguridad del administrador. Originalmente, el acceso era directo; se modificó para incluir un flujo de validación de PIN y una pantalla de "Configuración Inicial" que se dispara reactivamente si el DataStore detecta que no hay credenciales registradas.
* **Implementación de la mejora:** [Ver AdminViewModel.kt - verificarConfiguracion](PON_AQUÍ_TU_PERMALINK)

### RA1.g Asociación de eventos
**Justificación:** Gestión completa de eventos de usuario (clicks, gestos, entrada de texto) vinculados a lógica de negocio. Se utilizan callbacks eficientes para la edición de eventos, borrado con confirmación y ejecución de llamadas de emergencia mediante el sistema de permisos de Android.

### RA1.h App integrada
**Justificación:** La aplicación es un producto funcional y estable (v1.0.0). Se gestionan errores de flujo mediante el operador `.catch` de Kotlin Flow y se asegura la persistencia ante reinicios mediante `WorkManager` para las notificaciones programadas.

## 2. Interfaces Naturales de Usuario - NUI (RA2)

### RA2.a Herramientas NUI
**Justificación:** Se han identificado y analizado diversas herramientas del ecosistema Android para implementar interfaces naturales que reduzcan la carga cognitiva del usuario:
* **Reconocimiento de voz:** Uso de la API `SpeechRecognizer` de Android y los servicios de Google para la transcripción de audio a texto.
* **Detección Biométrica y Facial:** Uso de `ML Kit` (Google) para el análisis de expresiones y `BiometricPrompt` para seguridad.
* **Realidad Aumentada:** Análisis de `ARCore` para la superposición de información en el mundo real.
* **Interacción Háptica:** Uso de `VibratorManager` y `VibrationEffect` para comunicación táctil.

### RA2.b Diseño conceptual NUI
**Justificación:** El diseño NUI de MemoryAid se centra en la **invisibilidad de la interfaz**. Para un usuario con deterioro cognitivo, navegar por menús complejos es una barrera. El diseño conceptual propone que la app "escuche" y "sienta" al usuario. Se prioriza el feedback sensorial (vibración) y la entrada de datos simplificada (voz/gestos) para que la tecnología se adapte al humano y no al revés.

### RA2.c Interacción por voz (Propuesta técnica)
**Justificación:** Se plantea la integración de un asistente de voz mediante `SpeechToText`.
* **Caso de uso:** El usuario mantiene pulsado un botón de "Dictado" en el registro de eventos. La app captura el flujo de audio, lo procesa mediante `RecognizerIntent` y vuelca el resultado en el campo de descripción.
* **Beneficio:** Elimina la necesidad de utilizar el teclado virtual, lo cual es crítico para usuarios con dificultades motoras o fatiga visual.

### RA2.d Interacción por gesto
**Justificación:** Implementación de **Gestos Táctiles y Hápticos**.
* **Implementación real:** Se ha integrado un sistema de feedback táctil mediante `VibrationEffect`. Al confirmar una acción crítica (guardar registro), el dispositivo emite un patrón de vibración específico.
* **Propuesta adicional:** Implementación de gestos de movimiento mediante el acelerómetro (Shake to SOS), permitiendo que un movimiento brusco del terminal dispare la llamada de emergencia sin necesidad de buscar el botón en pantalla.
* **Código de referencia:** [Ver ejecución de vibración en RegistroViewModel/Screen](URL_A_TU_PERMALINK)

### RA2.e Detección facial/corporal (Propuesta técnica)
**Justificación:** Uso de `ML Kit Face Detection` para monitorización del estado de ánimo.
* **Planteamiento:** La app podría realizar una captura silenciosa al abrirse para analizar la expresión facial del paciente.
* **Utilidad:** Si se detectan signos de agitación o tristeza de forma recurrente, el sistema generaría una alerta automática en el panel del administrador, permitiendo una intervención temprana del cuidador basada en datos biométricos objetivos.


### RA2.f Realidad aumentada (Propuesta técnica)
**Justificación:** Uso de `ARCore` para la orientación espacial dentro del hogar.
* **Propuesta:** Implementación de "Anclajes de Ayuda". Mediante la cámara, la app puede reconocer la habitación en la que se encuentra el usuario y proyectar flechas virtuales en el suelo que guíen al paciente hacia el botiquín o la cocina.
* **Impacto:** Reduce la desorientación espacial, uno de los síntomas más estresantes en etapas tempranas de demencia, proporcionando autonomía al usuario en su entorno conocido.

## 3. Componentes de Software Reutilizables (RA3)

### RA3.a Herramientas de componentes
**Justificación:** Se han utilizado las herramientas nativas de **Jetpack Compose** para la creación y gestión de componentes.
* **Composables:** Funciones anotadas con `@Composable` que permiten la creación de UI modular.
* **Component Inspector:** Uso del inspector de diseño de Android Studio para verificar la jerarquía de componentes.
* **Previews:** Implementación de `@Preview` con diferentes configuraciones (Dark Mode, diferentes idiomas) para validar la integridad de los componentes de forma aislada.

### RA3.b Componentes reutilizables
**Justificación:** Se ha seguido una estrategia de diseño atómico para extraer elementos comunes. Componentes como `StatCard`, `ResumenDiarioWidget`, `BuscadorBar` y `EventoItem` residen en un paquete independiente (`ui.components`), permitiendo su uso en múltiples pantallas sin duplicar lógica.
* **Implementación:** [Ver paquete ui.components](URL_A_TU_REPOSITORIO/app/src/main/java/com/alberti/memoryaid/ui/components)

### RA3.c Parámetros y defaults
**Justificación:** Los componentes están diseñados para ser flexibles mediante el paso de parámetros. Se hace un uso intensivo de **valores por defecto** para simplificar su implementación:
* **Modificadores:** Todos los componentes aceptan un `modifier: Modifier = Modifier`, permitiendo al padre controlar el layout sin romper el encapsulamiento del hijo.
* **Estados iniciales:** Uso de valores por defecto en tipos de datos primitivos y estados de carga para asegurar que el componente sea funcional "out of the box".
* **Código de referencia:** [Ver parámetros en EventoItem.kt](URL_A_TU_PERMALINK)

### RA3.d Eventos en componentes
**Justificación:** Se implementa el patrón **State Hoisting** para la gestión de eventos. Los componentes no gestionan su propia lógica de negocio; en su lugar, exponen lambdas (callbacks) para que el padre decida la acción.
* **Ejemplo:** `EventoItem` expone `alEliminar: () -> Unit` y `alEditar: () -> Unit`. Esto permite que el mismo componente se comporte de forma distinta en el Dashboard o en un histórico.
* **Fluidez:** El uso de lambdas asegura que la interacción sea natural y no bloquee el hilo de la interfaz de usuario.

### RA3.f Documentación
**Justificación:** La documentación de los componentes se aborda en dos niveles:
1. **Documentación Técnica:** Uso de comentarios KDoc en las funciones `@Composable` para detallar el propósito de cada parámetro.
2. **Manual Técnico:** Detalle de la estructura de componentes en el archivo `docs/MANUAL.md`, facilitando la comprensión a otros desarrolladores que deseen extender la funcionalidad.

### RA3.h Integración en la app
**Justificación:** La integración es total y consistente. El componente `StatCard`, por ejemplo, se integra en el panel de administración para mostrar métricas, demostrando que la lógica de visualización está desacoplada de la fuente de datos. La app se percibe como un sistema cohesionado gracias a que los componentes comparten el mismo sistema de diseño y estados.

## 4. Usabilidad y Estándares (RA4)

### RA4.a Estándares
**Justificación:** La aplicación se adhiere estrictamente a los estándares de **Material Design 3**. Se implementa el uso de componentes semánticos y un sistema de temas dinámico que respeta las guías de accesibilidad (WCAG), asegurando ratios de contraste adecuados para usuarios con visión reducida.
* **Componentes:** Uso de `Scaffold`, `TopAppBar` y `FloatingActionButton` según las especificaciones de Google.
* **Accesibilidad:** Soporte para TalkBack mediante descripciones de contenido (`contentDescription`) en todos los elementos iconográficos.

### RA4.b Valoración de estándares
**Justificación:** La adopción de Material 3 no es estética, sino funcional. Permite una **curva de aprendizaje nula** para el usuario de Android, ya que los patrones de interacción (deslizar, tocar, navegar) son familiares. Esto es crítico en MemoryAid, donde el perfil del usuario (cuidador/paciente) requiere interfaces predecibles y seguras.

### RA4.c Menús
**Justificación:** Se ha implementado un sistema de navegación jerárquico y contextual:
* **Acceso Directo:** Uso de un `LargeFloatingActionButton` para la acción principal (Nuevo Registro).
* **Navegación de Gestión:** Menú de administración protegido por PIN en la `TopAppBar`, separando claramente el área de uso diario del área de configuración técnica.
* **Navegación de Retroceso:** Implementación de patrones de navegación predecibles mediante `onBack` en pantallas secundarias.

### RA4.d Distribución de acciones
**Justificación:** Las acciones se distribuyen según su prioridad y frecuencia de uso para reducir la carga cognitiva:
* **Acción Crítica:** El botón de "Aviso de Emergencia" ocupa una posición prominente y diferenciada por color (errorContainer) en la parte superior del Dashboard.
* **Acciones Secundarias:** Las opciones de edición y borrado están contenidas dentro de cada `EventoItem`, manteniendo el contexto de la acción.

### RA4.e Distribución de controles
**Justificación:** Se aplica una **jerarquía visual** estricta. Los controles más importantes se sitúan en la zona de alcance natural del pulgar (zona inferior). Se utiliza un espaciado consistente (`spacedBy(12.dp)`) y márgenes generosos (`20.dp`) para evitar pulsaciones accidentales, cumpliendo con el estándar de tamaño mínimo de objetivo táctil (48x48 dp).

### RA4.f Elección de controles
**Justificación:** Selección precisa de componentes según la función:
* **Entrada de datos:** `OutlinedTextField` con etiquetas flotantes para no perder el contexto durante la escritura.
* **Validación:** Uso de `CircularProgressIndicator` para feedback de procesos asíncronos en el panel de administración.
* **Visualización:** Empleo de `StatCard` para agrupar métricas, facilitando el escaneo visual rápido de datos clínicos.

### RA4.g Diseño visual
**Justificación:** Uso de una paleta de colores profesional y sobria. El color primario (Azul) transmite confianza, mientras que los colores de error se reservan exclusivamente para acciones destructivas o de emergencia. La tipografía utiliza pesos variados (`Bold` para títulos, `Medium` para contenido) para guiar el ojo del usuario a través de la información.


### RA4.h Claridad de mensajes
**Justificación:** La comunicación con el usuario es directa y no técnica.
* **Feedback de error:** Los errores en la validación del PIN se muestran inmediatamente bajo el campo de texto con un lenguaje claro ("PIN incorrecto").
* **Confirmación:** Uso de diálogos de confirmación antes de acciones irreversibles (purga de datos o borrado de eventos), describiendo las consecuencias exactas de la acción.

### RA4.i Pruebas de usabilidad
**Justificación:** Se han realizado evaluaciones de usabilidad basadas en escenarios de usuario (User Journeys). Se analizó la facilidad con la que un nuevo usuario puede configurar el contacto de emergencia y realizar un registro. Esto derivó en mejoras como el diálogo de "Configuración Requerida" al intentar acceder al panel de administración por primera vez, guiando al usuario proactivamente.

### RA4.j Evaluación en dispositivos
**Justificación:** La interfaz se ha validado en diferentes densidades de pantalla y orientaciones mediante el uso de `Preview` de Android Studio. La implementación de layouts elásticos y componentes que se adaptan al ancho del contenedor asegura que MemoryAid sea funcional tanto en dispositivos compactos como en terminales de gran formato.

## 5. Generación de Informes (RA5)

### RA5.a Establece la estructura del informe
**Justificación:** El informe clínico generado por MemoryAid sigue una estructura jerárquica y profesional diseñada para facilitar la lectura por parte de personal sanitario. El documento se organiza en:
* **Cabecera:** Identificación del informe y marca de tiempo de generación.
* **Resumen Estadístico:** Totales de actividad (medicación administrada vs. crisis registradas).
* **Cuerpo Detallado:** Listado cronológico de eventos con sus respectivas notas y categorías.
* **Pie de Informe:** Información de contacto de emergencia configurada en el sistema.

### RA5.b Genera informes a partir de fuentes de datos
**Justificación:** Se implementa el caso de uso `GenerarInformeUseCase`, el cual actúa como orquestador de datos. Este componente consume información directamente del `EventoRepository` (fuente persistente en Room), transforma las entidades de base de datos en strings formateados y permite su exportación mediante un `Intent.ACTION_SEND`, integrándose con el sistema de compartición nativo de Android.
* **Flujo técnico:** Data Layer (Room) → Domain Layer (UseCase) → Presentation Layer (ViewModel).

### RA5.c Establece filtros sobre los valores a presentar
**Justificación:** El sistema de informes no es estático; permite discriminar la información mediante filtros lógicos antes de la generación.
* **Implementación:** El `AdminViewModel` permite filtrar los datos que se enviarán al informe basándose en criterios temporales (ej. actividad de la última semana) y por tipología de evento (TipoEvento), asegurando que el informe clínico sea relevante para el periodo de consulta solicitado.

### RA5.d Incluye valores calculados, recuentos o totales
**Justificación:** La capa de dominio procesa los datos en bruto para generar métricas de valor clínico.
* **Cálculos realizados:** Se realizan recuentos automáticos de tomas de medicación, frecuencia de crisis epilépticas/caídas y totales acumulados por categoría.
* **Lógica de negocio:** Estos totales se calculan de forma reactiva en el `AdminViewModel` y se presentan tanto en la interfaz (StatCards) como en el informe exportable, proporcionando una visión cuantitativa del estado del paciente.

### RA5.e Incluye gráficos generados a partir de los datos
**Justificación:** Para mejorar la interpretabilidad de los datos, se ha desarrollado el componente `GraficoComparativo`.
* **Tecnología:** Uso de Jetpack Compose Canvas para dibujar representaciones visuales de la frecuencia de eventos.
* **Funcionalidad:** El gráfico permite comparar visualmente la relación entre la medicación (adherencia) y las crisis (efectividad), permitiendo detectar patrones de salud de forma inmediata que no serían evidentes en un listado textual.

## 6. Ayuda y Documentación (RA6)

### RA6.a Identifica sistemas de generación de ayudas
**Justificación:** Se han seleccionado formatos estándar de la industria para garantizar la portabilidad y accesibilidad de la ayuda. Se utiliza **Markdown (.md)** por su capacidad de renderizado nativo en plataformas como GitHub y su facilidad de conversión a otros formatos como PDF o HTML. Además, se integran sistemas de ayuda en tiempo real mediante componentes de la interfaz de Android (Tooltips y Diálogos contextuales).

### RA6.b Genera ayudas en formatos habituales
**Justificación:** La documentación técnica y de usuario se ha generado siguiendo una estructura profesional de archivos:
* **README.md:** Punto de entrada al repositorio con instrucciones de despliegue y galería visual.
* **MANUAL.md:** Documento exhaustivo que combina la guía de usuario y las especificaciones técnicas.
* **CRITERIOS.md:** Este documento, que actúa como guía de referencia para la evaluación del software.

### RA6.c Genera ayudas sensibles al contexto
**Justificación:** La aplicación implementa lógica de ayuda que reacciona al estado del sistema.
* **Ejemplo:** El diálogo de "Configuración Inicial" aparece automáticamente cuando un usuario intenta acceder al panel de administración sin un PIN previo. Esta ayuda guía al usuario exactamente en el punto donde se encuentra el bloqueo, eliminando la necesidad de consultar manuales externos para operaciones básicas.

### RA6.d Documenta la estructura de la información persistente
**Justificación:** La estructura de persistencia se basa en una base de datos relacional gestionada por Room. Se ha documentado la entidad principal `EventoMemoria`, detallando sus campos (`id`, `titulo`, `tipo`, `fecha`, etc.) y sus tipos de datos. Esta documentación asegura que el esquema sea comprensible para futuras extensiones o migraciones de datos.

### RA6.e Manual de usuario y guía de referencia
**Justificación:** Se incluye un manual detallado en `docs/MANUAL.md` que describe las funcionalidades clave: registro de eventos, configuración de alarmas y visualización de métricas. Se han utilizado capturas de pantalla y pasos numerados para facilitar la comprensión por parte del cuidador, minimizando el riesgo de errores operativos.

### RA6.f Manual técnico de instalación/configuración
**Justificación:** El manual técnico proporciona instrucciones claras para la puesta en marcha:
* **Entorno:** Requisitos de SDK de Android y Gradle.
* **Dependencias:** Listado de librerías críticas (Hilt, Room, Compose).
* **Instalación:** Guía para la carga del archivo APK en dispositivos físicos, incluyendo la gestión de permisos necesarios (Cámara, Teléfono, Notificaciones).

### RA6.g Confecciona tutoriales
**Justificación:** Se ha diseñado un flujo de "Onboarding" implícito. La aplicación guía al usuario mediante estados de UI vacíos (`Empty States`) que sugieren la primera acción a realizar cuando no hay datos. Asimismo, los diálogos paso a paso para la creación de PIN y configuración de contacto actúan como micro-tutoriales interactivos que aseguran que el dispositivo quede correctamente configurado antes de su uso clínico.

## 7. Pruebas y Calidad (RA8)

### RA8.a Estrategia de pruebas
**Justificación:** Se ha implementado una estrategia de pruebas basada en la **Pirámide de Pruebas**, priorizando la estabilidad y la mantenibilidad del código. La estrategia se divide en:
* **Pruebas Unitarias:** Validación de la lógica de negocio en Use Cases y ViewModels de forma aislada.
* **Pruebas de Integración:** Verificación de la persistencia en la base de datos Room y la comunicación entre capas.
* **Herramientas:** Uso de **JUnit 5** para la ejecución de tests y **Mockito/Mockk** para el simulado de dependencias, asegurando que los tests sean deterministas y rápidos.

### RA8.b Pruebas de integración
**Justificación:** Se han realizado pruebas de integración para asegurar que los componentes del sistema interactúan correctamente sin fugas de datos o estados inconsistentes.
* **Capa de Datos:** Validación de los DAOs de Room para asegurar que las consultas (Queries) y transacciones de los eventos de memoria se ejecutan correctamente en el hilo de IO.
* **Flujo Completo:** Verificación de que el `SessionManager` persiste correctamente el PIN en el DataStore y que este cambio es detectado reactivamente por el `AdminViewModel` para actualizar la interfaz de usuario.


### RA8.g Documentación de pruebas
**Justificación:** La infraestructura de pruebas está integrada en el sistema de construcción del proyecto (Gradle).
* **Localización:** Los tests unitarios residen en `src/test` y los tests de instrumentación en `src/androidTest`, siguiendo las convenciones oficiales de Android.
* **Informes:** Se utiliza la capacidad de generación de informes de Gradle para documentar el porcentaje de éxito de las pruebas. Cada Use Case crítico cuenta con una suite de pruebas que cubre tanto el "camino feliz" (success) como la gestión de excepciones, garantizando la robustez de MemoryAid ante entradas de datos inesperadas.