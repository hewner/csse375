/**

<p>Provides an extension mechanism for the application's user interface.
Extension-specific user interface components can be integrated into the
main CleanSheets user interface. Each extension can specify a
<code>UIExtension</code> sub-class that provides any of the pre-defined
types of components. Sub-classes should override the accessor methods for the
components that they provide, and add components as selection listeners whenever
required.

@see csheets.ext

*/
package csheets.ui.ext;