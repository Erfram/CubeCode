package com.cubecode.client.views.idea.utils;

import com.cubecode.CubeCodeClient;
import com.cubecode.utils.ColorUtils;
import imgui.extension.texteditor.TextEditorLanguageDefinition;
import imgui.extension.texteditor.flag.TextEditorPaletteIndex;

import java.util.*;
import java.util.regex.Pattern;

public class ScriptDefinition {
    public static TextEditorLanguageDefinition typeScript() {
        return new TextEditorLanguageDefinition();
    }

    public static TextEditorLanguageDefinition html() {
        return new TextEditorLanguageDefinition();
    }

    public static TextEditorLanguageDefinition css() {
        return new TextEditorLanguageDefinition();
    }

    public static TextEditorLanguageDefinition python() {
        return new TextEditorLanguageDefinition();
    }

    public static TextEditorLanguageDefinition cs() {
        return new TextEditorLanguageDefinition();
    }

    public static TextEditorLanguageDefinition bsh() {
        return new TextEditorLanguageDefinition();
    }

    public static TextEditorLanguageDefinition xml() {
        return new TextEditorLanguageDefinition();
    }

    public static TextEditorLanguageDefinition xaml() {
        return new TextEditorLanguageDefinition();
    }

    public static TextEditorLanguageDefinition json() {
        return new TextEditorLanguageDefinition();
    }

    public static TextEditorLanguageDefinition toml() {
        return new TextEditorLanguageDefinition();
    }

    public static TextEditorLanguageDefinition javaScript() {
        String[] keywords = new String[] {
                "var", "let",
                "const", "function", "if",
                "else", "for",
                "while", "do",
                "break", "continue",
                "return", "switch",
                "case", "default",
                "new", "delete",
                "typeof", "instanceof",
                "in", "true", "false",
                "null", "undefined",
                "try", "catch",
                "finally", "throw",
                "this", "with"
        };

        Map<String, Integer> tokenRegex = new HashMap<>();

        tokenRegex.put("L?\\\"(\\\\.|[^\\\"])*\\\"", TextEditorPaletteIndex.String);
        tokenRegex.put("\\'\\\\?[^\\']\\'", TextEditorPaletteIndex.String);

        tokenRegex.put("\\b\\d+(\\.\\d+)?\\b", TextEditorPaletteIndex.Number);
        tokenRegex.put("[\\[\\]\\{\\}\\!\\%\\^\\&\\*\\(\\)\\-\\+\\=\\~\\|\\<\\>\\?\\/\\;\\,\\.]", TextEditorPaletteIndex.Punctuation);

        Map<String, String> identifiers = new HashMap<>();

        identifiers.put("Function", "The Function object provides methods for functions. In JavaScript, every function is actually a Function object.");
        identifiers.put("Object", "The Object type represents one of JavaScript's data types. It is used to store various keyed collections and more complex entities. Objects can be created using the Object() constructor or the object initializer / literal syntax.");
        identifiers.put("Error", "Error objects are thrown when runtime errors occur. The Error object can also be used as a base object for user-defined exceptions. See below for standard built-in error types.");
        identifiers.put("CallSite", "Object representing the location of the function call in the code. Stores information about the call context.");
        identifiers.put("decodeURI", "The decodeURI() function decodes a Uniform Resource Identifier (URI) previously created by encodeURI() or a similar routine.");
        identifiers.put("decodeURIComponent", "The decodeURIComponent() function decodes a Uniform Resource Identifier (URI) component previously created by encodeURIComponent() or by a similar routine.");
        identifiers.put("encodeURI", "The encodeURI() function encodes a URI by replacing each instance of certain characters by one, two, three, or four escape sequences representing the UTF-8 encoding of the character (will only be four escape sequences for characters composed of two surrogate characters). Compared to encodeURIComponent(), this function encodes fewer characters, preserving those that are part of the URI syntax.");
        identifiers.put("encodeURIComponent", "The encodeURIComponent() function encodes a URI by replacing each instance of certain characters by one, two, three, or four escape sequences representing the UTF-8 encoding of the character (will only be four escape sequences for characters composed of two surrogate characters). Compared to encodeURI(), this function encodes more characters, including those that are part of the URI syntax.");
        identifiers.put("escape", "The escape() function computes a new string in which certain characters have been replaced by hexadecimal escape sequences.");
        identifiers.put("eval", "The eval() function evaluates JavaScript code represented as a string and returns its completion value. The source is parsed as a script.");
        identifiers.put("isFinite", "The isFinite() function determines whether a value is finite, first converting the value to a number if necessary. A finite number is one that's not NaN or ±Infinity. Because coercion inside the isFinite() function can be surprising, you may prefer to use Number.isFinite().");
        identifiers.put("isNan", "The isNaN() function determines whether a value is NaN, first converting the value to a number if necessary. Because coercion inside the isNaN() function can be surprising, you may prefer to use Number.isNaN().");
        identifiers.put("isXMLName", "A function that checks if the string is a valid XML name.");
        identifiers.put("parseFloat", "The parseFloat() function parses a string argument and returns a floating point number.");
        identifiers.put("parseInt", "The parseInt() function parses a string argument and returns an integer of the specified radix (the base in mathematical numeral systems).");
        identifiers.put("unescape", "The unescape() function computes a new string in which hexadecimal escape sequences are replaced with the characters that they represent. The escape sequences might be introduced by a function like escape().");
        identifiers.put("uneval", "The uneval() is an inbuilt function in JavaScript that is used to create a string representation of the source code of an Object.");
        identifiers.put("EvalError", "The EvalError object indicates an error regarding the global eval() function. This exception is not thrown by JavaScript anymore, however the EvalError object remains for compatibility.");
        identifiers.put("RangeError", "The RangeError object indicates an error when a value is not in the set or range of allowed values.");
        identifiers.put("ReferenceError", "The ReferenceError object represents an error when a variable that doesn't exist (or hasn't yet been initialized) in the current scope is referenced.");
        identifiers.put("SyntaxError", "The SyntaxError object represents an error when trying to interpret syntactically invalid code. It is thrown when the JavaScript engine encounters tokens or token order that does not conform to the syntax of the language when parsing code.");
        identifiers.put("TypeError", "The TypeError object represents an error when an operation could not be performed, typically (but not exclusively) when a value is not of the expected type.");
        identifiers.put("URIError", "The URIError object represents an error when a global URI handling function was used in a wrong way.");
        identifiers.put("InternalError", "The InternalError object indicates an error that occurred internally in the JavaScript engine.");
        identifiers.put("JavaException", "Wrapper for Java exceptions in JavaScript code. Allows to intercept and process Java exceptions in Rhino.");
        identifiers.put("Array", "The Array object, as with arrays in other programming languages, enables storing a collection of multiple items under a single variable name, and has members for performing common array operations.");
        identifiers.put("String", "The String object is used to represent and manipulate a sequence of characters.");
        identifiers.put("Boolean", "Boolean values can be one of two values: true or false, representing the truth value of a logical proposition.");
        identifiers.put("Number", "Number values represent floating-point numbers like 37 or -9.25.\nThe Number constructor contains constants and methods for working with numbers. Values of other types can be converted to numbers using the Number() function.");
        identifiers.put("Date", "JavaScript Date objects represent a single moment in time in a platform-independent format. Date objects encapsulate an integral number that represents milliseconds since the midnight at the beginning of January 1, 1970, UTC (the epoch).");
        identifiers.put("Math", "The Math namespace object contains static properties and methods for mathematical constants and functions.\nMath works with the Number type. It doesn't work with BigInt.");
        identifiers.put("JSON", "JavaScript Object Notation (JSON) is a standard text-based format for representing structured data based on JavaScript object syntax. It is commonly used for transmitting data in web applications (e.g., sending some data from the server to the client, so it can be displayed on a web page, or vice versa).");
        identifiers.put("With", "Operator for temporarily adding object properties to the scope. It is used to simplify access to object properties.");
        identifiers.put("Call", "Object representing a function call. It contains information about the called function and its parameters.");
        identifiers.put("Iterator", "Interface for enumerating elements of a collection in JavaScript. Provides next(), hasNext() methods.");
        identifiers.put("StopIteration", "Special exception that signals the end of the iteration.");
        identifiers.put("RegExp", "The RegExp object is used for matching text with a pattern.\nFor an introduction to regular expressions, read the Regular expressions chapter in the JavaScript guide. For detailed information of regular expression syntax, read the regular expression reference.");
        identifiers.put("Symbol", "Symbol is a built-in object whose constructor returns a symbol primitive — also called a Symbol value or just a Symbol — that's guaranteed to be unique. Symbols are often used to add unique property keys to an object that won't collide with keys any other code might add to the object, and which are hidden from any mechanisms other code will typically use to access the object. That enables a form of weak encapsulation, or a weak form of information hiding.");
        identifiers.put("Map", "The Map object holds key-value pairs and remembers the original insertion order of the keys. Any value (both objects and primitive values) may be used as either a key or a value.");
        identifiers.put("Set", "The Set object lets you store unique values of any type, whether primitive values or object references.");
        identifiers.put("WeakMap", "A WeakMap is a collection of key/value pairs whose keys must be objects or non-registered symbols, with values of any arbitrary JavaScript type, and which does not create strong references to its keys. That is, an object's presence as a key in a WeakMap does not prevent the object from being garbage collected. Once an object used as a key has been collected, its corresponding values in any WeakMap become candidates for garbage collection as well — as long as they aren't strongly referred to elsewhere. The only primitive type that can be used as a WeakMap key is symbol — more specifically, non-registered symbols — because non-registered symbols are guaranteed to be unique and cannot be re-created.");
        identifiers.put("JavaAdapter", "Mechanism for implementing Java interfaces and inheritance of Java classes in JavaScript code. Allows you to create JavaScript objects that can be used as Java objects.");
        identifiers.put("Java", "Global object for working with Java classes");
        identifiers.put("CubeCode", "Utility Global object that is needed to create something (vector, matrix), or it contains utility methods");

//        regex.put(collectRegex(declarations), TextEditorPaletteIndex.Preprocessor); // DECLARATIONS
//        regex.put(collectRegex(controlFlow), TextEditorPaletteIndex.PreprocIdentifier); // CONTROL_FLOW
//        regex.put(collectRegex(operators), TextEditorPaletteIndex.Breakpoint); // OPERATORS
//        regex.put(collectRegex(values), TextEditorPaletteIndex.ErrorMarker); // VALUES
//        regex.put(collectRegex(cubeCodeObjects), TextEditorPaletteIndex.CharLiteral); // CUBECODE_OBJECTS
//        regex.put(collectRegex(errorHandling), TextEditorPaletteIndex.KnownIdentifier); // ERROR_HANDLING
//        regex.put(collectRegex(objects), TextEditorPaletteIndex.MultiLineComment); // OBJECTS

        TextEditorLanguageDefinition langDef = TextEditorLanguageDefinition.c();

        langDef.setKeywords(keywords);
        langDef.setTokenRegexStrings(tokenRegex);
        langDef.setIdentifiers(identifiers);

        langDef.setCommentStart("/*");
        langDef.setCommentEnd("*/");
        langDef.setSingleLineComment("//");

        langDef.setAutoIdentation(false);

        langDef.setName("JavaScript");;

        return langDef;
    }

    public static String collectRegex(String[] words) {
        StringBuilder regex = new StringBuilder();
        regex.append("(?:(?:^|[^a-zA-Z0-9_])(");

        regex.append(String.join("|", words));

        regex.append("))(?![a-zA-Z0-9_])");
        return regex.toString();
    }

	public static int[] getJavaScriptPalette() {
        short[] windowBg = CubeCodeClient.themeManager.currentTheme.windowBg;
        short[] text = CubeCodeClient.themeManager.currentTheme.text;

        return new int[]{
                0xff7f7f7f,	// Default
                ColorUtils.rgbaToImguiColor(253, 150, 34, 255),	// Keyword
                ColorUtils.rgbaToImguiColor(172, 140, 255, 255),	// Number
                ColorUtils.rgbaToImguiColor(40, 219, 88, 255),	// String
                ColorUtils.rgbaToImguiColor(78, 201, 176, 255), // Char literal
                ColorUtils.rgbaToImguiColor(text[0], text[1], text[2], text[3]), // Punctuation
                ColorUtils.rgbaToImguiColor(86, 156, 214, 255), //Preprocessor
                ColorUtils.rgbaToImguiColor(text[0], text[1], text[2], text[3]), // Identifier
                ColorUtils.rgbaToImguiColor(204, 120, 50, 255), // Known identifier
                ColorUtils.rgbaToImguiColor(197, 134, 192, 255), // Preproc identifier
                ColorUtils.rgbaToImguiColor(116, 112, 93, 255), // Comment (single line)
                ColorUtils.rgbaToImguiColor(156, 220, 254, 255), // Comment (multi line)
                ColorUtils.rgbaToImguiColor(39, 40, 34, 150), // Background
                0xffe0e0e0, // Cursor
                0x80a06020, // Selection
                ColorUtils.rgbaToImguiColor(172, 140, 255, 255), // ErrorMarker
                ColorUtils.rgbaToImguiColor(255, 99, 99, 255), // Breakpoint
                ColorUtils.rgbaToImguiColor(76, 81, 74, 255), // Line number
                0x40000000, // Current line fill
                0x40808080, // Current line fill (inactive)
                ColorUtils.rgbaToImguiColor(128, 128, 128, 255), // Current line edge
        };
    }
}