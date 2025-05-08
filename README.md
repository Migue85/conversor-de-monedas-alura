Conversor de monedas

Esta APP esta desarrollada en lenguaje JAVA, permite realizar de forma sencilla la cotización de varias monedas y poder comprar monedas de distitnos países de latino america con el dólar norte americano.

se bva a ver un menú de interacción formado de la siguiente manera

1.  Dólar (USD) => Peso Argentino (ARS)
2.  Peso Argentino (ARS) => Dólar (USD)
3.  Dólar (USD) => Real Brasilero (BRL)
4.  Real Brasilero (BRL) => Dólar (USD)
5.  Dólar (USD) => Peso Colombiano (COP)
6.  Peso Colombiano (COP) => Dólar (USD)
7.  Salir de la aplicación

El usuario puede seleccionar una de estas opciones y luego ingresar el valor a convertir. La APP mostrará el resultado de la conversión 

Paso a paso su forma de uso

1.  La aplicación mostrará un menú numerado con las opciones de conversión disponibles.
(Ya indicado anteriormente)
2.  Ingresa el número de la opción de conversión que deseas realizar y luego presiona Enter.

3.  La aplicación solicitara que ingreses el valor numérico que deseas cotizar, ingresa el valor y luego presiona Enter.

4.  La APP mostrará el resultado en el formato: `El valor de [valor ingresado] [sigla moneda origen] corresponde al valor final de => [valor convertido] [sigla moneda destino]`.

5.  El menú se mostrará nuevamente, permitiéndo realizar otra cotizacion o la opción 7 para salir de la APP.

La APP incluye un manejo de errores para ciertos casos:


* Opción inválida en el menú.

* Error al consumir la API (Ejemplo: problemas de red, API inválida, La API devuelve un error).

* No se obtuvo tasa de cotizacion para la moneda seleccionada (EjemplO: la API no soporta la conversión solicitada).

## Nota

* La precisión de las tasas de cambio depende de la API utilizada y puede variar.

* Esta es una APP de consola básica para demostrar la funcionalidad.
