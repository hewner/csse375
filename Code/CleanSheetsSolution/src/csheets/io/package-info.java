/**

<p>Provides support for importing and exporting workbooks to and from various
file formats. Classes that can read and/or write <code>Workbook</code>s
to/from files of a certain format should implement the <code>Codec</code>
interface. <code>Codec</code>s must be named <code>nnnCodec</code> where
<code>nnn</code> is the extension of files of that format.

<p>Also provided is a factory for accessing codecs, and some I/O related
utility classes.

*/
package csheets.io;