# xNoInsecureChat
![Java](https://img.shields.io/badge/Java-17-blue?logo=java&logoColor=white) ![Spigot](https://img.shields.io/badge/API-Spigot%2FPaper%201.19.1%2B-orange.svg) ![ProtocolLib](https://img.shields.io/badge/Requiere-ProtocolLib-red.svg)

Un plugin simple y ligero que elimina el molesto mensaje de "Chat no seguro" (y "Missing Profile Key") que aparece al unirse a servidores 1.19.1 en adelante.
desarrollado por **xPlugins**

## 🚀 Características
* **Elimina el mensaje** de "Chat no seguro".
* **Ligero:** No consume casi nada de rendimiento.
* **Configurable:** Puedes activar o desactivar la función desde la `config.yml`.
* **Comando de Recarga:** `/nic reload` (o `/noinsecurechat reload`) para recargar la configuración sin reiniciar.
* **API para Developers:** Incluye métodos para que tus otros plugins puedan controlar este.

## ⛓️ Dependencias
* Java 17
* Servidor Spigot o Paper (1.19.1 en adelante)
* **ProtocolLib** (¡Obligatorio! El plugin no funcionará sin él)

## 🛠️ API
Puedes usar la API del plugin para controlar el filtro desde tus propios plugins:

```java
// Importa la clase de tu API
import jn.willfrydev.noinsecurechat.xNoInsecureChat;

// Obtén la instancia del plugin (puedes hacer esto en tu onEnable)
xNoInsecureChat api = xNoInsecureChat.getInstance();

// Ejemplo de uso

// Comprueba si el filtro está activado
if (api.isFilterEnabled()) {
    
    // Desactívalo si es necesario
    api.setFilterEnabled(false);
    System.out.println("El filtro de chat ha sido desactivado desde mi otro plugin.");
    
} else {
    
    // Actívalo si estaba apagado
    api.setFilterEnabled(true);
    System.out.println("El filtro de chat ha sido activado desde mi otro plugin.");
}
