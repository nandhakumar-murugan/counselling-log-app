package com.example

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.core.content.FileProvider
import java.io.BufferedReader
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.InputStreamReader
import java.io.OutputStream
import java.io.OutputStreamWriter
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

object LocalFileManager {
    private const val TAG = "LocalFileManager"
    const val FILE_NAME = "Counseling_Data.csv"
    
    val HEADERS = listOf(
        "S.No", 
        "Date", 
        "Case ID", 
        "Name", 
        "Age", 
        "Gender", 
        "Department", 
        "Year", 
        "Contact No", 
        "Referral Source", 
        "Presenting Problem", 
        "Category of Issue", 
        "Severity Level", 
        "First Visit / Follow-up", 
        "Mode of Counselling", 
        "Sessions Conducted", 
        "Intervention Used", 
        "Referred to Specialist", 
        "Assessment", 
        "Follow-Up Date", 
        "Progress Status", 
        "Outcome", 
        "Next Plan",
        "College Name",
        "Semester",
        "Hostel",
        "Student Email"
    )

    // Cached rows loaded from secondary storage
    private val cachedRows = mutableListOf<List<String>>()

    fun bootstrap(context: Context) {
        try {
            val exists = checkFileExists(context)
            if (!exists) {
                createFileWithHeaders(context)
            }
            loadIntoCache(context)
        } catch (e: Exception) {
            Log.e(TAG, "Error bootstrapping LocalFileManager", e)
        }
    }

    private fun checkFileExists(context: Context): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val resolver = context.contentResolver
            val uri = MediaStore.Downloads.EXTERNAL_CONTENT_URI
            val projection = arrayOf(MediaStore.Downloads._ID)
            val selection = "${MediaStore.Downloads.DISPLAY_NAME} = ?"
            val selectionArgs = arrayOf(FILE_NAME)
            
            resolver.query(uri, projection, selection, selectionArgs, null)?.use { cursor ->
                if (cursor.moveToFirst()) {
                    return true
                }
            }
        } else {
            val file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), FILE_NAME)
            return file.exists()
        }
        return false
    }

    private fun createFileWithHeaders(context: Context) {
        writeAllRows(context, listOf(HEADERS))
    }

    fun getCachedRows(): List<List<String>> {
        synchronized(cachedRows) {
            return ArrayList(cachedRows)
        }
    }

    fun appendRow(context: Context, row: List<String>): Boolean {
        synchronized(cachedRows) {
            try {
                // Ensure cache lies flat with current file state
                loadIntoCacheInternal(context)
                
                // Add new data line
                cachedRows.add(row)
                
                // Persist down to external storage
                val success = writeAllRows(context, cachedRows)
                if (success) {
                    Log.d(TAG, "Substituted spreadsheet file with appended row")
                }
                return success
            } catch (e: Exception) {
                Log.e(TAG, "Failed appending row", e)
                return false
            }
        }
    }

    fun loadIntoCache(context: Context) {
        synchronized(cachedRows) {
            loadIntoCacheInternal(context)
        }
    }

    private fun loadIntoCacheInternal(context: Context) {
        val lines = readRows(context)
        cachedRows.clear()
        if (lines.isNotEmpty()) {
            cachedRows.addAll(lines)
        } else {
            cachedRows.add(HEADERS)
            writeAllRows(context, cachedRows)
        }
    }

    private fun readRows(context: Context): List<List<String>> {
        val rows = mutableListOf<List<String>>()
        var inputStream: InputStream? = null
        
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val resolver = context.contentResolver
                val uri = MediaStore.Downloads.EXTERNAL_CONTENT_URI
                val projection = arrayOf(MediaStore.Downloads._ID)
                val selection = "${MediaStore.Downloads.DISPLAY_NAME} = ?"
                val selectionArgs = arrayOf(FILE_NAME)
                
                var fileUri: Uri? = null
                resolver.query(uri, projection, selection, selectionArgs, null)?.use { cursor ->
                    if (cursor.moveToFirst()) {
                        val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Downloads._ID)
                        val id = cursor.getLong(idColumn)
                        fileUri = Uri.withAppendedPath(uri, id.toString())
                    }
                }
                
                if (fileUri != null) {
                    inputStream = resolver.openInputStream(fileUri!!)
                }
            } else {
                val file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), FILE_NAME)
                if (file.exists()) {
                    inputStream = file.inputStream()
                }
            }

            inputStream?.let { stream ->
                BufferedReader(InputStreamReader(stream)).use { reader ->
                    var line: String? = reader.readLine()
                    while (line != null) {
                        if (line.trim().isNotEmpty()) {
                            rows.add(parseCsvLine(line))
                        }
                        line = reader.readLine()
                    }
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error executing CSV reading stream", e)
        } finally {
            try {
                inputStream?.close()
            } catch (ignored: Exception) {}
        }
        
        // If we found zero files/rows in shared, check modern sandbox fallback and sync
        if (rows.isEmpty()) {
            // Check if directory fallback exists
            val sandboxFile = File(context.filesDir, FILE_NAME)
            if (sandboxFile.exists()) {
                try {
                    sandboxFile.inputStream().use { stream ->
                        BufferedReader(InputStreamReader(stream)).use { reader ->
                            var line: String? = reader.readLine()
                            while (line != null) {
                                if (line.trim().isNotEmpty()) {
                                    rows.add(parseCsvLine(line))
                                }
                                line = reader.readLine()
                            }
                        }
                    }
                    // Attempt to write down to shared downloads so it's easily exposed
                    writeAllRows(context, rows)
                } catch (e: Exception) {
                    Log.e(TAG, "Error reading fall-back sandboxed file", e)
                }
            }
        }
        
        return rows
    }

    private fun parseCsvLine(line: String): List<String> {
        val result = mutableListOf<String>()
        val currentToken = StringBuilder()
        var insideQuotes = false
        var i = 0
        while (i < line.length) {
            val c = line[i]
            if (c == '"') {
                if (insideQuotes && i + 1 < line.length && line[i + 1] == '"') {
                    currentToken.append('"')
                    i++
                } else {
                    insideQuotes = !insideQuotes
                }
            } else if (c == ',') {
                if (insideQuotes) {
                    currentToken.append(c)
                } else {
                    result.add(currentToken.toString())
                    currentToken.setLength(0)
                }
            } else {
                currentToken.append(c)
            }
            i++
        }
        result.add(currentToken.toString())
        return result
    }

    private fun escapeCsvField(field: String): String {
        val clean = field.replace("\n", " ").replace("\r", " ").trim()
        if (clean.contains(",") || clean.contains("\"") || clean.contains("'")) {
            return "\"" + clean.replace("\"", "\"\"") + "\""
        }
        return clean
    }

    private fun writeAllRows(context: Context, rows: List<List<String>>): Boolean {
        var outputStream: OutputStream? = null
        var success = false
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val resolver = context.contentResolver
                val uri = MediaStore.Downloads.EXTERNAL_CONTENT_URI
                
                val projection = arrayOf(MediaStore.Downloads._ID)
                val selection = "${MediaStore.Downloads.DISPLAY_NAME} = ?"
                val selectionArgs = arrayOf(FILE_NAME)
                
                var fileUri: Uri? = null
                resolver.query(uri, projection, selection, selectionArgs, null)?.use { cursor ->
                    if (cursor.moveToFirst()) {
                        val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Downloads._ID)
                        val id = cursor.getLong(idColumn)
                        fileUri = Uri.withAppendedPath(uri, id.toString())
                    }
                }

                if (fileUri == null) {
                    val contentValues = ContentValues().apply {
                        put(MediaStore.Downloads.DISPLAY_NAME, FILE_NAME)
                        put(MediaStore.Downloads.MIME_TYPE, "text/csv")
                        put(MediaStore.Downloads.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
                    }
                    fileUri = resolver.insert(uri, contentValues)
                }

                fileUri?.let { activeUri ->
                    outputStream = resolver.openOutputStream(activeUri, "rwt")
                }
            } else {
                val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                if (!downloadsDir.exists()) downloadsDir.mkdirs()
                val file = File(downloadsDir, FILE_NAME)
                outputStream = FileOutputStream(file)
            }

            outputStream?.let { stream ->
                OutputStreamWriter(stream).use { writer ->
                    rows.forEach { rowFields ->
                        val line = rowFields.joinToString(",") { escapeCsvField(it) }
                        writer.write(line)
                        writer.write("\n")
                    }
                }
                success = true
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error performing CSV write", e)
        } finally {
            try {
                outputStream?.close()
            } catch (ignored: Exception) {}
        }
        
        // Also always write to secure app internal storage for fallback safety!
        try {
            val sandboxFile = File(context.filesDir, FILE_NAME)
            sandboxFile.outputStream().use { stream ->
                OutputStreamWriter(stream).use { writer ->
                    rows.forEach { rowFields ->
                        val line = rowFields.joinToString(",") { escapeCsvField(it) }
                        writer.write(line)
                        writer.write("\n")
                    }
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed safeguarding local internal backup", e)
        }
        
        return success
    }

    fun getShareableFileUri(context: Context, customRows: List<List<String>>? = null): Uri? {
        try {
            val rows = if (customRows != null) {
                val list = mutableListOf<List<String>>()
                list.add(HEADERS)
                list.addAll(customRows)
                list
            } else {
                getCachedRows()
            }
            if (rows.isEmpty()) return null

            // Write to a temporary file inside our cache directory
            val cacheFile = File(context.cacheDir, "Shared_$FILE_NAME")
            if (cacheFile.exists()) {
                cacheFile.delete()
            }
            cacheFile.createNewFile()
            
            cacheFile.outputStream().use { stream ->
                OutputStreamWriter(stream).use { writer ->
                    rows.forEach { rowFields ->
                        val line = rowFields.joinToString(",") { escapeCsvField(it) }
                        writer.write(line)
                        writer.write("\n")
                    }
                }
            }

            return FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                cacheFile
            )
        } catch (e: Exception) {
            Log.e(TAG, "Error assembling export provider share-sheet path", e)
            return null
        }
    }

    // Analytics Helper Functions
    fun isDateInCurrentWeek(dateStr: String): Boolean {
        return try {
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
            val date = sdf.parse(dateStr.trim()) ?: return false
            
            val today = Calendar.getInstance()
            val recordCal = Calendar.getInstance().apply { time = date }
            
            // Calculate starting of week (Sunday or Monday) matches
            val currentWeek = today.get(Calendar.WEEK_OF_YEAR)
            val currentYear = today.get(Calendar.YEAR)
            
            recordCal.get(Calendar.WEEK_OF_YEAR) == currentWeek && recordCal.get(Calendar.YEAR) == currentYear
        } catch (e: Exception) {
            false
        }
    }

    fun isDateInCurrentMonth(dateStr: String): Boolean {
        return try {
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
            val date = sdf.parse(dateStr.trim()) ?: return false
            
            val today = Calendar.getInstance()
            val recordCal = Calendar.getInstance().apply { time = date }
            
            recordCal.get(Calendar.MONTH) == today.get(Calendar.MONTH) && 
            recordCal.get(Calendar.YEAR) == today.get(Calendar.YEAR)
        } catch (e: Exception) {
            false
        }
    }

    fun updateRow(context: Context, index: Int, updatedRow: List<String>): Boolean {
        synchronized(cachedRows) {
            try {
                loadIntoCacheInternal(context)
                if (index < 0 || index >= cachedRows.size) return false
                cachedRows[index] = updatedRow
                return writeAllRows(context, cachedRows)
            } catch (e: Exception) {
                Log.e(TAG, "Failed updating row at index $index", e)
                return false
            }
        }
    }
}
