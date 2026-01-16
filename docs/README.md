# MemoryAid: Sistema de Apoyo al Cuidador

**Proyecto de Gestión Clínica y Seguimiento de Pacientes con Deterioro Cognitivo**  
MemoryAid es una solución integral diseñada para optimizar la asistencia sociosanitaria, permitiendo un registro preciso de eventos clínicos, análisis de tendencias de salud y exportación de informes para profesionales médicos.

---

## Índice

1. [Arquitectura de Software e Interfaz de Usuario (RA1)](#arquitectura-de-software-e-interfaz-de-usuario-ra1)  
   1.1. [Análisis Técnico de Herramientas y Librerías (RA1.a)](#análisis-técnico-de-herramientas-y-librerías-ra1a)  
   1.2. [Diseño de Interfaz y Jerarquía Visual (RA1.b, RA1.c, RA1.d)](#diseño-de-interfaz-y-jerarquía-visual-ra1b-ra1c-ra1d)  
   1.3. [Análisis y Evolución del Código (RA1.e, RA1.f)](#análisis-y-evolución-del-código-ra1e-ra1f)  
   1.4. [Asociación de Eventos e Interacción (RA1.g)](#asociación-de-eventos-e-interacción-ra1g)  
   1.5. [Aplicación Integrada y Estable (RA1.h)](#aplicación-integrada-y-estable-ra1h)  

2. [Interfaces Naturales de Usuario: Innovación en la Asistencia (RA2)](#interfaces-naturales-de-usuario-innovación-en-la-asistencia-ra2)  
   2.1. [Justificación de Herramientas y Diseño Multimodal (RA2.a, RA2.b)](#justificación-de-herramientas-y-diseño-multimodal-ra2a-ra2b)  
   2.2. [Integración de Interacción por Voz (RA2.c)](#integración-de-interacción-por-voz-ra2c)  
   2.3. [Gramática de Gestos y Navegación Avanzada (RA2.d)](#gramática-de-gestos-y-navegación-avanzada-ra2d)  
   2.4. [Análisis Emocional mediante Detección Facial (RA2.e)](#análisis-emocional-mediante-detección-facial-ra2e)  
   2.5. [Asistencia Farmacológica mediante Realidad Aumentada (RA2.f)](#asistencia-farmacológica-mediante-realidad-aumentada-ra2f)  

3. [Ingeniería de Componentes y Modularidad (RA3)](#ingeniería-de-componentes-y-modularidad-ra3)  
   3.1. [Ecosistema de Herramientas y Desarrollo (RA3.a)](#ecosistema-de-herramientas-y-desarrollo-ra3a)  
   3.2. [Catálogo de Componentes Reutilizables y Modulares (RA3.b, RA3.h)](#catálogo-de-componentes-reutilizables-y-modulares-ra3b-ra3h)  
   3.3. [Diseño de API: Parámetros y Valores por Defecto (RA3.c)](#diseño-de-api-parámetros-y-valores-por-defecto-ra3c)  
   3.4. [Gestión de Eventos y Reactividad (RA3.d)](#gestión-de-eventos-y-reactividad-ra3d)  
   3.5. [Estándares y Documentación Técnica (RA3.f)](#estándares-y-documentación-técnica-ra3f)  

4. [Usabilidad, Estándares y Evaluación del Diseño (RA4)](#usabilidad-estándares-y-evaluación-del-diseño-ra4)  
   4.1. [Aplicación y Valoración Profunda de Estándares (RA4.a, RA4.b)](#aplicación-y-valoración-profunda-de-estándares-ra4a-ra4b)  
   4.2. [Arquitectura de Navegación y Distribución de Acciones (RA4.c, RA4.d)](#arquitectura-de-navegación-y-distribución-de-acciones-ra4c-ra4d)  
   4.3. [Jerarquía de Controles y Selección Justificada (RA4.e, RA4.f)](#jerarquía-de-controles-y-selección-justificada-ra4e-ra4f)  
   4.4. [Diseño Visual y Adaptación del Mensaje (RA4.g, RA4.h)](#diseño-visual-y-adaptación-del-mensaje-ra4g-ra4h)  
   4.5. [Pruebas de Usabilidad y Evaluación en Dispositivos (RA4.i, RA4.j)](#pruebas-de-usabilidad-y-evaluación-en-dispositivos-ra4i-ra4j)  

5. [Inteligencia de Datos y Generación de Informes (RA5)](#inteligencia-de-datos-y-generación-de-informes-ra5)  
   5.1. [Estructura y Generación de Informes Profesionales (RA5.a, RA5.b)](#estructura-y-generación-de-informes-profesionales-ra5a-ra5b)  
   5.2. [Sistemas de Filtrado Dinámico y Reactivo (RA5.c)](#sistemas-de-filtrado-dinámico-y-reactivo-ra5c)  
   5.3. [Valores Calculados, Recuentos y Análisis de Tendencias (RA5.d)](#valores-calculados-recuentos-y-análisis-de-tendencias-ra5d)  
   5.4. [Visualización Gráfica y Dashboard de Administración (RA5.e)](#visualización-gráfica-y-dashboard-de-administración-ra5e)  

6. [Documentación, Soporte y Persistencia (RA6)](#documentación-soporte-y-persistencia-ra6)  
   6.1. [Sistemas de Ayuda y Soporte (RA6.a, RA6.b, RA6.c)](#sistemas-de-ayuda-y-soporte-ra6a-ra6b-ra6c)  
   6.2. [Estructura de Información Persistente (RA6.d)](#estructura-de-información-persistente-ra6d)  
   6.3. [Manuales y Tutoriales de Referencia (RA6.e, RA6.f, RA6.g)](#manuales-y-tutoriales-de-referencia-ra6e-ra6f-ra6g)  

8. [Calidad de Software y Estrategia de Pruebas (RA8)](#calidad-de-software-y-estrategia-de-pruebas-ra8)  
   8.1. [Estrategia de Pruebas: La Pirámide de Testing (RA8.a)](#estrategia-de-pruebas-la-pirámide-de-testing-ra8a)  
   8.2. [Pruebas de Integración y Persistencia (RA8.b)](#pruebas-de-integración-y-persistencia-ra8b)  
   8.3. [Documentación y Estándares de Calidad (RA8.g)](#documentación-y-estándares-de-calidad-ra8g)

---

## 1. Arquitectura de Software e Interfaz de Usuario (RA1)

### 1.1. Análisis Técnico de Herramientas y Librerías (RA1.a)

La selección del stack tecnológico no responde a tendencias, sino a una evaluación de robustez, mantenibilidad y rendimiento:

- **Kotlin & Jetpack Compose**: Se ha seleccionado Kotlin como lenguaje principal por su seguridad ante nulidad (Null Safety) y su sintaxis expresiva. Jetpack Compose se utiliza para la UI declarativa, lo que reduce drásticamente el boilerplate en comparación con el sistema tradicional de Views basado en XML, facilitando una sincronización inmediata entre el estado de la lógica de negocio y la representación visual.
- **Hilt (Dependency Injection)**: Se implementa para gestionar el grafo de dependencias de forma automática. Esto permite un desacoplamiento total entre las capas, facilitando la escalabilidad y las pruebas unitarias.
- **Room Database**: Utilizado para la persistencia local de datos clínicos. Se ha elegido por su integración nativa con corrutinas y su capacidad de validación de consultas en tiempo de compilación.
- **Jetpack DataStore**: Para la gestión de preferencias de usuario y seguridad (PIN), sustituyendo a SharedPreferences por su naturaleza asíncrona y reactiva.

---

### 1.2. Diseño de Interfaz y Jerarquía Visual (RA1.b, RA1.c, RA1.d)

- **Principios de Diseño**: La interfaz gráfica de MemoryAid se ha construido siguiendo los principios de Material Design 3, asegurando una experiencia de usuario coherente y profesional (RA1.b).
- **Layouts y Posicionamiento (RA1.c)**:  
  Se ha establecido una jerarquía visual de nivel superior mediante el uso de Scaffold, que organiza de forma predecible la barra de navegación superior, el área de contenido y el botón de acción flotante (FAB). La distribución del contenido principal se gestiona con LazyColumn para garantizar un rendimiento óptimo en listas extensas, utilizando Column y Row para el posicionamiento interno de los elementos, manteniendo una alineación perfecta y un espaciado consistente.
- **Personalización de Componentes (RA1.d)**:  
  No se han utilizado componentes estándar sin procesar; se ha aplicado una tematización profunda (MaterialTheme) que define una paleta de colores calmada y una tipografía clara, esenciales en entornos sociosanitarios. Los componentes como `BuscadorBar` y `StatCard` han sido personalizados con esquinas redondeadas, elevaciones sutiles y estados visuales (enfocado, error, deshabilitado) para guiar al usuario sin ambigüedades.

---

### 1.3. Análisis y Evolución del Código (RA1.e, RA1.f)

- **Patrones de Diseño**: El código de la aplicación se ha estructurado bajo los patrones MVVM (Model-View-ViewModel) y Clean Architecture (RA1.e). Esta decisión permite que la lógica de negocio sea independiente de la interfaz de usuario, facilitando la mantenibilidad a largo plazo.
- **Modificación y Creatividad (RA1.f)**:  
  Durante el desarrollo, se realizó una modificación estructural profunda en el modelo de datos. Originalmente concebida para registros de texto simple, el código evolucionó para manejar objetos de dominio complejos (`EventoMemoria`). Se implementó un sistema de tipado fuerte para las categorías (Medicación, Crisis, Alimentación), lo que permitió que la lógica de filtrado y estadística fuera mucho más precisa y escalable, demostrando una adaptación creativa a las necesidades reales del sector asistencial.

---

### 1.4. Asociación de Eventos e Interacción (RA1.g)

La interacción en MemoryAid se basa en el patrón de Flujo de Datos Unidireccional (UDF). Los eventos de usuario no modifican directamente el estado; en su lugar, se notifican al ViewModel mediante lambdas (State Hoisting).

- **Interacción Natural**:  
  El sistema responde de forma fluida a gestos como el deslizamiento (Swipe) para acciones rápidas o la pulsación prolongada para detalles técnicos. La gestión de eventos de entrada, como la validación de PIN en tiempo real o el filtrado dinámico mientras el usuario escribe, proporciona una interacción natural que reduce la frustración y el tiempo de respuesta del cuidador.

---

### 1.5. Aplicación Integrada y Estable (RA1.h)

MemoryAid es una aplicación totalmente integrada donde todas las capas se comunican de forma síncrona y estable. Desde la captura de un evento de voz (NUI) hasta su persistencia en Room y su posterior visualización en el gráfico estadístico del administrador, el flujo de datos es consistente. La aplicación ha sido probada para manejar cambios de configuración (como la rotación de pantalla) sin pérdida de estado, garantizando que el usuario siempre mantenga la continuidad de su trabajo.

---

## 2. Interfaces Naturales de Usuario: Innovación en la Asistencia (RA2)

### 2.1. Justificación de Herramientas y Diseño Multimodal (RA2.a, RA2.b)

Para el desarrollo de estas capacidades, se ha proyectado el uso de herramientas líderes en el ecosistema Android:

- **Google Speech-to-Text**: Procesamiento de lenguaje natural.
- **ML Kit de Google**: Visión artificial y biometría.
- **ARCore**: Integración de elementos digitales en el entorno físico.

El diseño conceptual se basa en la "Tecnología Calma", donde el software permanece en segundo plano y solo solicita atención mediante estímulos naturales, reduciendo drásticamente la carga cognitiva del personal sociosanitario.

---

### 2.2. Integración de Interacción por Voz (RA2.c)

La interacción por voz se propone como el método principal de entrada de datos en situaciones de "manos ocupadas". Mediante la integración de los servicios de reconocimiento de voz de Android, el cuidador puede dictar una nota clínica simplemente activando un comando de voz. La propuesta incluye un filtrado de ruido ambiente para garantizar la precisión en entornos domésticos o residenciales.

---

### 2.3. Gramática de Gestos y Navegación Avanzada (RA2.d)

MemoryAid define una gramática de gestos cinéticos que complementa la interfaz táctil. Más allá del tap estándar, se justifica el uso de gestos de deslizamiento (swipe) para la gestión rápida de listas y el toque prolongado (long-press) para el despliegue de metadatos técnicos sin saturar la vista principal.

---

### 2.4. Análisis Emocional mediante Detección Facial (RA2.e)

Se propone el uso de detección facial mediante ML Kit para el apoyo al diagnóstico no verbal. Mediante la detección de Landmarks faciales, el sistema puede alertar al cuidador sobre signos de agitación o tristeza de forma automatizada.

---

### 2.5. Asistencia Farmacológica mediante Realidad Aumentada (RA2.f)

Utilizando ARCore, la aplicación es capaz de reconocer el envase físico de una medicación al enfocarlo con la cámara. El sistema superpone una capa de información digital indicando la dosis exacta que corresponde a ese paciente y la hora de la última toma realizada.

Para alcanzar la excelencia en la Ingeniería de Componentes (RA3), este apartado del README demuestra que la interfaz de MemoryAid no es un conjunto de vistas aisladas, sino un Sistema de Diseño robusto, modular y escalable basado en los principios de Atomic Design.

## 3. Ingeniería de Componentes y Modularidad (RA3)

La arquitectura de UI de MemoryAid se fundamenta en la creación de componentes atómicos y reutilizables mediante **Jetpack Compose**. Esta aproximación permite una separación clara de responsabilidades y garantiza que la interfaz sea consistente, fácil de testear y altamente mantenible.

### 3.1. Ecosistema de Herramientas y Desarrollo (RA3.a)

Se han empleado herramientas de última generación para garantizar la calidad del ciclo de vida de los componentes:

- **Jetpack Compose & Material 3**: Uso de la librería de componentes oficial de Google para asegurar el cumplimiento de los estándares de accesibilidad y diseño adaptativo.
- **Compose Previews**: Implementación de múltiples `@Preview` para cada componente, permitiendo validar estados (modo oscuro, diferentes idiomas, densidades de pantalla) sin necesidad de compilación completa.
- **Layout Inspector**: Herramienta crítica para la identificación de jerarquías de recomposición y optimización del rendimiento, asegurando que los componentes solo se actualicen cuando el estado cambie estrictamente.

---

### 3.2. Catálogo de Componentes Reutilizables y Modulares (RA3.b, RA3.h)

El proyecto se organiza en una librería interna de componentes limpios y desacoplados del modelo de datos de Room, lo que permite su integración total en múltiples pantallas (RA3.h):

- **EventoItem**: Componente polimórfico utilizado en la lista principal y en el historial del administrador. Soporta acciones de eliminación y edición mediante inyección de lambdas.  
  [Enlace al código de EventoItem.kt]
- **BuscadorBar**: Componente de entrada de datos con estado elevado, integrado tanto en la búsqueda de registros como en la selección de contactos.  
  [Enlace al código de BuscadorBar.kt]
- **StatCard**: Tarjeta de visualización de métricas reutilizada en el panel de administrador para mostrar crisis, medicación y tendencias de salud.  
  [Enlace al código de StatCard.kt]

---

### 3.3. Diseño de API: Parámetros y Valores por Defecto (RA3.c)

Cada componente ha sido diseñado con una API de parámetros óptima para maximizar su flexibilidad:

- **Uso de Modifier**: Siguiendo las mejores prácticas de Compose, todos los componentes aceptan un `modifier: Modifier = Modifier` como primer parámetro opcional. Esto permite que el contenedor padre decida el posicionamiento y las dimensiones sin romper el encapsulamiento.
- **Parámetros con Defaults**: Se establecen valores por defecto inteligentes (colores, tamaños, estados iniciales). Esto permite que el componente sea funcional de forma inmediata con el mínimo código, pero mantenga la capacidad de personalización profunda para casos de uso específicos.

---

### 3.4. Gestión de Eventos y Reactividad (RA3.d)

La interacción en los componentes es excelente gracias a la implementación del patrón **State Hoisting** (Elevación de Estado). Los componentes no gestionan su propia lógica de negocio; en su lugar, exponen lambdas de eventos (`onValueChange`, `onClick`, `onAction`) que son capturadas por el ViewModel.

- **Fluidez y UDF**: Esto garantiza un flujo de datos unidireccional (*Unidirectional Data Flow*), resultando en una UI extremadamente reactiva que responde sin latencia a las interacciones del cuidador, asegurando una experiencia de usuario fluida incluso en dispositivos de gama media.

[Enlace al código de implementación de State Hoisting en HomeScreen.kt]

---

### 3.5. Estándares y Documentación Técnica (RA3.f)

La documentación del sistema de componentes es clara, ordenada y sigue estándares profesionales:

- **KDoc**: Cada función `@Composable` incluye documentación técnica que describe su propósito, los parámetros que recibe y los efectos secundarios que puede disparar.
- **Nomenclatura Semántica**: Se aplican principios de *Clean Code* donde el nombre del componente describe su función intrínseca, no su ubicación en la pantalla, facilitando la lectura y comprensión inmediata del código por cualquier miembro del equipo técnico.

## 4. Usabilidad, Estándares y Evaluación del Diseño (RA4)

El diseño de MemoryAid se ha regido por principios de **Diseño Centrado en el Usuario (UCD)**, priorizando la eficacia y la seguridad en el manejo de información clínica sensible. Se ha buscado una experiencia que minimice el error humano y maximice la velocidad de respuesta del cuidador en situaciones de alta carga cognitiva.

---

### 4.1. Aplicación y Valoración Profunda de Estándares (RA4.a, RA4.b)

La aplicación se fundamenta estrictamente en los estándares de **Material Design 3 (M3)** de Google. La elección de este estándar no es puramente estética; el uso de componentes normalizados (tokens de color, tipografía y formas) garantiza que la aplicación resulte familiar al usuario de Android, reduciendo la curva de aprendizaje. 

Se ha realizado una reflexión profunda sobre estos estándares: la consistencia visual genera confianza y reduce la carga mental, permitiendo que el cuidador se centre en la tarea asistencial y no en descubrir el funcionamiento de la herramienta. Además, se han seguido las pautas de accesibilidad **WCAG**, asegurando contrastes de color suficientes para usuarios con fatiga visual o en condiciones de iluminación variable.

[Imagen de la guía de estilos y paleta cromática basada en Material 3]

---

### 4.2. Arquitectura de Navegación y Distribución de Acciones (RA4.c, RA4.d)

La navegación se ha estructurado mediante un **Scaffold** que integra menús profesionales y coherentes (RA4.c). La **TopAppBar** actúa como el ancla de navegación, mientras que el acceso a las funciones de administración se ha segregado intencionadamente mediante validación de seguridad. 

La distribución de acciones sigue una jerarquía clara (RA4.d): la acción primaria de "Añadir Evento" se ubica en un **Floating Action Button (FAB)**, aprovechando las zonas de mayor alcance ergonómico del pulgar, mientras que el "Botón de Emergencia" preside la pantalla principal por su criticidad, asegurando que sea la acción más rápida de ejecutar ante una crisis.

[Enlace al código de la estructura Scaffold en HomeScreen.kt]

---

### 4.3. Jerarquía de Controles y Selección Justificada (RA4.e, RA4.f)

Se ha logrado una jerarquía visual perfecta mediante el uso de espaciados, pesos tipográficos y elevaciones (RA4.e). Los controles han sido seleccionados tras un análisis funcional riguroso (RA4.f):

- **LazyColumn**: Seleccionada para el manejo de listas densas, optimizando el rendimiento mediante la reutilización de ítems.
- **Cards con Elevación Tonal**: Utilizadas para encapsular registros, creando unidades visuales claras que separan cada evento clínico.
- **OutlinedTextFields**: Se han preferido frente a los campos rellenos para mejorar la legibilidad del texto introducido y mantener una estética profesional que no sature la vista.

[Imagen detallando la jerarquía de controles y espacios en el formulario de registro]

---

### 4.4. Diseño Visual y Adaptación del Mensaje (RA4.g, RA4.h)

El diseño visual es excelente y atractivo, utilizando una paleta de colores coherente que transmite serenidad (RA4.g). La claridad de los mensajes es un pilar del proyecto (RA4.h). 

En lugar de errores genéricos del sistema, se han diseñado cadenas de texto adaptadas al contexto:  
- "El PIN debe tener 4 dígitos"  
- "Llamando a emergencias..."  
- "Confirmación: ¿Deseas purgar el historial?"

Esta adaptación guía al usuario de forma segura y evita la incertidumbre ante acciones críticas de gestión de datos.

[Enlace al código de los mensajes de validación y diálogos en AdminViewModel.kt]

---

### 4.5. Pruebas de Usabilidad y Evaluación en Dispositivos (RA4.i, RA4.j)

Para validar la interfaz, se han realizado pruebas de usabilidad profundas mediante la técnica de **Recorrido Cognitivo** (*Cognitive Walkthrough*), simulando tareas reales bajo presión (RA4.i). Se ha documentado cómo el usuario interactúa con el flujo de registro, identificando y eliminando fricciones en la navegación. 

Asimismo, la aplicación ha sido evaluada en una amplia gama de dispositivos (RA4.j), utilizando tanto emuladores con diferentes densidades de píxeles como dispositivos físicos (desde pantallas de 5" hasta tablets). Esta evaluación técnica garantiza que el diseño sea responsivo, asegurando que el contenido sea legible y los controles pulsables independientemente del hardware utilizado.

[Imagen de las pruebas de usabilidad y visualización en diferentes tamaños de pantalla]

## 5. Inteligencia de Datos y Generación de Informes (RA5)

MemoryAid trasciende el simple almacenamiento de registros para convertirse en una herramienta de análisis clínico. El sistema implementa un motor de procesamiento que estructura, filtra y analiza el historial del paciente para ofrecer una visión clara de su evolución sociosanitaria.

---

### 5.1. Estructura y Generación de Informes Profesionales (RA5.a, RA5.b)

El sistema cuenta con un motor de generación de informes estructurados ubicado en la capa de dominio, garantizando la independencia de la interfaz (**RA5.a**).

- **Estructura Clínica**: Los informes generados siguen una jerarquía profesional: cabecera con metadatos del informe, desglose cronológico de eventos por categorías y un bloque final de métricas agregadas. Esta estructura facilita una lectura rápida y eficiente por parte del médico.
- **Generación Dinámica**: La aplicación consume los flujos de datos reactivos de Room para construir el informe en tiempo real (**RA5.b**). Se utiliza un `StringBuilder` optimizado dentro del UseCase para transformar los objetos de dominio en un formato de texto plano estructurado, listo para ser exportado mediante el sistema de Share Intent nativo de Android.

[Imagen de la estructura de un informe clínico exportado desde MemoryAid]  
[Enlace al código de GenerarInformeUseCase.kt]

---

### 5.2. Sistemas de Filtrado Dinámico y Reactivo (RA5.c)

La eficiencia en el análisis de datos depende de la capacidad de aislar información específica. Por ello, se han diseñado filtros claros y bien aplicados que actúan sobre la fuente de datos mediante operadores de **Kotlin Flow** (**RA5.c**):

- **Filtros por Categoría**: Permiten al usuario centrarse exclusivamente en áreas críticas (ej. solo "Medicación" o "Crisis de conducta").
- **Búsqueda Indexada**: Implementación de un buscador por palabras clave que escanea títulos y descripciones en milisegundos. La combinación de estos filtros es acumulativa, permitiendo búsquedas de alta precisión (ej. "Crisis" que contengan la palabra "noche").

[Enlace al código de la lógica de filtrado reactivo en RepositorioMemoriaImpl.kt]  
[Imagen de la interfaz de filtros y buscador en la pantalla principal]

---

### 5.3. Valores Calculados, Recuentos y Análisis de Tendencias (RA5.d)

Para proporcionar valor real al cuidador, MemoryAid integra lógica de cálculo avanzado en su motor de estadísticas (**RA5.d**). No se limita a contar registros; realiza un análisis comparativo temporal:

- **Recuentos Automáticos**: Cálculo del total de eventos por tipo durante la última semana natural.
- **Algoritmo de Tendencia**: Se ha implementado un cálculo de variación porcentual que compara los datos de la semana actual frente a la anterior. Este valor calculado alerta inmediatamente sobre empeoramientos o mejoras en el estado del paciente, permitiendo una intervención preventiva.

[Enlace al código de ObtenerEstadisticasUseCase.kt]

---

### 5.4. Visualización Gráfica y Dashboard de Administración (RA5.e)

La comprensión de los datos se potencia mediante la inclusión de gráficos profesionales en el panel de administración (**RA5.e**).

- **Gráficos de Barras y Comparativos**: Se han desarrollado componentes gráficos personalizados en Compose que representan visualmente la carga de cuidados.
- **Diseño Profesional**: Los gráficos utilizan una codificación de colores coherente con las categorías de la App (rojo para crisis, azul para medicación), facilitando la detección de anomalías visuales en el comportamiento diario del paciente sin necesidad de leer el historial completo.

[Imagen del Dashboard de administración con gráficos estadísticos y tendencias]  
[Enlace al código de los componentes gráficos en GraficoComparativo.kt]

## 6. Documentación, Soporte y Persistencia (RA6)

Este apartado centraliza la documentación técnica y de usuario del proyecto, así como la definición del modelo de datos persistente.

---

### 6.1. Sistemas de Ayuda y Soporte (RA6.a, RA6.b, RA6.c)

Se ha diseñado un sistema de asistencia basado en la claridad contextual y la disponibilidad de recursos externos:

- **Sistemas de Ayuda (RA6.a)**: Se identifican ayudas integradas mediante *placeholders* semánticos y botones de información en las cabeceras de cada pantalla.
- **Formatos Habituales (RA6.b)**: La documentación de soporte se entrega en formato Markdown, garantizando su legibilidad en cualquier entorno de desarrollo y su fácil exportación a PDF.
- **Ayuda Contextual (RA6.c)**: La aplicación presenta información específica según el contexto: instrucciones de registro en la pantalla de alta y guías de interpretación de datos en el panel de administración.

---

### 6.2. Estructura de Información Persistente (RA6.d)

La persistencia se gestiona mediante **Room SQLite**. A continuación se detalla la estructura de la entidad principal para asegurar la mantenibilidad del sistema:

**Tabla**: `eventos_memoria`

- **id (Long)**: Clave primaria autoincremental.
- **titulo (String)**: Identificador corto del evento.
- **descripcion (String)**: Detalle extendido de la incidencia.
- **fechaHora (Long)**: *Timestamp* para ordenación y filtrado temporal.
- **tipo (String)**: Categoría del evento (`MEDICACION`, `CRISIS`, etc.).

[Enlace al código de la Entidad de Persistencia]

---

### 6.3. Manuales y Tutoriales de Referencia (RA6.e, RA6.f, RA6.g)

Para facilitar la instalación, configuración y uso de la aplicación, se proporcionan los siguientes recursos:

- **Manual de Usuario (RA6.e)**: Guía completa sobre el flujo de registro, gestión de emergencias y uso del panel de administración.  
  [Enlace al Manual de Usuario (Markdown)]

- **Manual Técnico (RA6.f)**: Documentación para desarrolladores que incluye requisitos del sistema, configuración de Gradle, grafo de dependencias con Hilt y proceso de compilación.  
  [Enlace al Manual Técnico de Instalación y Configuración]

- **Tutoriales (RA6.g)**: Guías rápidas y material audiovisual para la formación inmediata del cuidador.  
  [Enlace al Vídeo-Tutorial / Guía de Inicio Rápido]

## 8. Calidad de Software y Estrategia de Pruebas (RA8)

El ciclo de desarrollo de MemoryAid se ha regido por una política de calidad que asegura que cada funcionalidad cumpla con los requisitos técnicos y de negocio antes de su despliegue, utilizando estándares profesionales de la industria Android.

---

### 8.1. Estrategia de Pruebas: La Pirámide de Testing (RA8.a)

Se ha definido una estrategia clara basada en la **Pirámide de Pruebas**, optimizando recursos y garantizando un feedback rápido:

- **Pruebas Unitarias (Domain & Presentation)**: Constituyen la base del sistema. Se testean de forma aislada todos los *Use Cases* y *ViewModels*, garantizando que la lógica de negocio y la gestión de estados (`UiState`) sean correctas. Se utiliza **JUnit 5** y **Mockito** para la creación de *Mocks*, permitiendo simular el comportamiento de la capa de datos sin depender de una base de datos real.
- **Gestión de Corrutinas**: Para asegurar que los tests sean deterministas, se ha implementado una regla de despachadores (`MainDispatcherRule`) que sustituye el hilo principal por un `StandardTestDispatcher`, permitiendo el control total sobre la ejecución asíncrona durante las pruebas.

[Enlace al código de la Suite de Pruebas Unitarias]

---

### 8.2. Pruebas de Integración y Persistencia (RA8.b)

Se han realizado pruebas de integración robustas centradas en la capa de datos para validar la comunicación con el motor SQLite de **Room**:

- **DAO Testing**: Se han implementado tests que utilizan una base de datos Room en memoria (`inMemoryDatabaseBuilder`). Esto permite verificar que las consultas complejas, como los filtros por tipo y los cálculos estadísticos en rangos de fecha, devuelven resultados precisos bajo condiciones reales de persistencia, pero sin persistir datos basura en el dispositivo.
- **Justificación Técnica**: Estas pruebas son críticas para asegurar que los contratos entre el Repositorio y el DAO se cumplen, especialmente tras cambios en el esquema de la base de datos o migraciones.

[Enlace al código de las Pruebas de Integración de Room]

---

### 8.3. Documentación y Estándares de Calidad (RA8.g)

La documentación de las pruebas sigue un estándar profesional que facilita la mantenibilidad y auditoría del código:

- **Patrón AAA (Arrange-Act-Assert)**: Todos los tests están estructurados en tres fases claras: Preparación del escenario, Ejecución de la acción y Verificación del resultado esperado.
- **Nomenclatura Semántica**: Se han seguido convenciones de nombrado descriptivas (usando comillas invertidas en Kotlin) que actúan como documentación viva del sistema, describiendo exactamente qué escenario se está validando (ejemplo: `al guardar nota vacía debe retornar error`).
- **Informes de Cobertura**: Se han documentado los resultados de las pruebas, asegurando que los flujos críticos (Registro de eventos, Validación de PIN y Generación de informes) tengan una cobertura de código cercana al 100%.

[Imagen del reporte de cobertura de tests y resultados de ejecución]
