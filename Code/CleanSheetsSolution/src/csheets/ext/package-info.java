/**

<p>Provides an extension mechanism for the application. The basic capabilities
of the CleanSheets application is extended by means of inheritance by
delegation. Two abstract base-classes (<code>CellExtension</code> and
<code>SpreadsheetExtension</code>) are provided that implement the
<code>Cell</code> and <code>Spreadsheet</code> interface respectively.
There, calls to methods in each interface are delegated to an instance of
<code>CellImpl</code> or <code>SpreadsheetImpl</code>. Both of these interfaces
extend the <code>Extensible</code> interface, whereby extensions can be accessed
by the <code>getExtension(String)</code> method.

<p>In order to improve on the existing functionality, extensions may provide
sub-classes of these two base-classes. Sub-classes are required to be serializable.

<p>An <code>Extension</code> has three parts. Apart from the two listed above (of
which at least one should be provided), all extensions should provide a 
<code>UIExtension</code> that acts as a facade for retrieving
extension-specific user interface components, to be integrated into the
application's user interface.

<p>Extensions are loaded dynamically on start-up using the class names listed in
the extension properties files. Known extensions are loaded if already in the
class path. If any of those conditions is not met, extensions should be listed
in a file named <code>extensions.xml</code> in the directory from which the
application is executed. The file is a standard XML properties files, where
entries for extensions should have the class name of the extension as key,
and an optional class path specification as value. The class path can be given
as a URL or local filename referring to a JAR-file or a directory where the
extension class can be found. (See example below.)

<pre>
&lt;?xml version="1.0" encoding="UTF-8"?&gt;
&lt;!DOCTYPE properties SYSTEM "http://java.sun.com/dtd/properties.dtd"&gt;
&lt;properties&gt;
&lt;entry key="csheets.ext.assertion.AssertionExtension"/&gt;
&lt;entry key="csheets.ext.test.TestExtension"&gt;test.jar&lt;/entry&gt;
&lt;/properties&gt;
</pre>

@see csheets.ui.ext

*/
package csheets.ext;