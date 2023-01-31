package com.unittesting;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    public static final int cellCount = 2;
    Button send_excel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        send_excel = findViewById(R.id.send_excel);

        send_excel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    selectfile();
                } else {
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 101);
                }
            }
            //ContextCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 101);
        });
    }

    // request for storage permission if not given
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 101) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                selectfile();
            } else {
                Toast.makeText(MainActivity.this, "permission not granted", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void selectfile() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(Intent.createChooser(intent, "select file "), 102);
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 102) {
            if (requestCode == RESULT_OK) {
                String filePath = data.getData().getPath();
                //if excel file then only select the file
                if (filePath.endsWith(".xlsx") || filePath.endsWith(".xls")) {
                    readFile(data.getData());
                }
            }
        }
    }

    ProgressDialog dialog;

    private void readFile(final Uri file) {
        dialog = new ProgressDialog(this);
        dialog.setMessage("uploading");
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                final HashMap<String, Object> parentmap = new HashMap<>();
                try {
                    HSSFWorkbook workbook;
                    // check for the input from the excel file
                    try (InputStream inputStream = getContentResolver().openInputStream(file)) {
                        workbook = new HSSFWorkbook(inputStream);
                    }
                    final String timestamp = "" + System.currentTimeMillis();
                    HSSFSheet sheet = workbook.getSheetAt(0);
                    FormulaEvaluator formulaEvaluator = workbook.getCreationHelper().createFormulaEvaluator();
                    int rowscount = sheet.getPhysicalNumberOfRows();
                    if (rowscount > 0) {
                        //check row wise data
                        for (int r = 0; r < rowscount; r++) {
                            Row row = sheet.getRow(r);
                            if (row.getPhysicalNumberOfCells() == cellCount) {
                                // get cell data
                                String A = getCellData(row, 0, formulaEvaluator);
                                String B = getCellData(row, 1, formulaEvaluator);

                                // initialise the hash map and put value of a and b into it
                                HashMap<String, Object> quetionmap = new HashMap<>();
                                quetionmap.put("A", A);
                                quetionmap.put("B", B);
                                String id = UUID.randomUUID().toString();
                                parentmap.put(id, quetionmap);
                            } else {
                                dialog.dismiss();
                                Toast.makeText(MainActivity.this, "row no. " + (r + 1) + " has incorrect data", Toast.LENGTH_LONG).show();
                                return;
                            }
                        }
                        // add the data in firebase if everything is correct
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // add the data according to timestamp
                                FirebaseDatabase.getInstance().getReference().child("Data").
                                        child(timestamp).updateChildren(parentmap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    dialog.dismiss();
                                                    Toast.makeText(MainActivity.this, "Uploaded Successfully", Toast.LENGTH_LONG).show();
                                                } else {
                                                    dialog.dismiss();
                                                    Toast.makeText(MainActivity.this, "something went wrong", Toast.LENGTH_LONG).show();
                                                }
                                            }

                                        });
                            }
                        });
                    }
                    // show the error if file is empty
                    else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.dismiss();
                                Toast.makeText(MainActivity.this, "File is empty", Toast.LENGTH_LONG).show();
                            }
                        });
                        return;
                    }

                }
                // show the error message if failed
                // due to file not
                catch (final FileNotFoundException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }

                    });

                }
                // show the error message if there
                // is error in input output
                catch (final IOException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });

    }
    private String getCellData(Row row, int cellposition, FormulaEvaluator formulaEvaluator) {

        String value = "";

        // get cell from excel sheet
        Cell cell = row.getCell(cellposition);
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_BOOLEAN:
                return value + cell.getBooleanCellValue();
            case Cell.CELL_TYPE_NUMERIC:
                return value + cell.getNumericCellValue();
            case Cell.CELL_TYPE_STRING:
                return value + cell.getStringCellValue();
            default:
                return value;
        }
    }

}


