package com.example.myapplication;

/**
 * Created by 강지희 on 2017-12-04.
 */
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Fragment2 extends Fragment {
    public Fragment2()
    {
        // required
    }
    public class DatabaseActivity extends AppCompatActivity {

        TextView idView;
        EditText productBox;
        EditText quantityBox;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.fragment_fragment2);

            idView = (TextView) findViewById(R.id.productID);
            productBox = (EditText) findViewById(R.id.productName);
            quantityBox =
                    (EditText) findViewById(R.id.productQuantity);
        }

        public void newProduct (View view) {
            MyDBHandler dbHandler = new MyDBHandler(this, null, null, 1);
            int quantity =
                    Integer.parseInt(quantityBox.getText().toString());
            Product product =
                    new Product(productBox.getText().toString(), quantity);
            dbHandler.addProduct(product);
            productBox.setText("");
            quantityBox.setText("");
        }

        public void lookupProduct (View view) {
            MyDBHandler dbHandler = new MyDBHandler(this, null, null, 1);
            Product product =
                    dbHandler.findProduct(productBox.getText().toString());
            if (product != null) {
                idView.setText(String.valueOf(product.getID()));
                quantityBox.setText(String.valueOf(product.getQuantity()));
            } else {
                idView.setText("No Match Found");
            }
        }

        public void removeProduct (View view) {
            MyDBHandler dbHandler = new MyDBHandler(this, null,
                    null, 1);
            boolean result = dbHandler.deleteProduct(
                    productBox.getText().toString());
            if (result)
            {
                idView.setText("Record Deleted");
                productBox.setText("");
                quantityBox.setText("");
            }
            else
                idView.setText("No Match Found");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        RelativeLayout layout = (RelativeLayout)inflater.inflate(R.layout.fragment_fragment2,
                container, false);
        return layout;
    }


    public class MyDBHandler extends SQLiteOpenHelper {

        private static final int DATABASE_VERSION = 1;
        private static final String DATABASE_NAME = "productDB.db";
        public static final String TABLE_PRODUCTS = "products";

        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_PRODUCTNAME = "productname";
        public static final String COLUMN_QUANTITY = "quantity";

        public MyDBHandler(Context context, String name,
                           SQLiteDatabase.CursorFactory factory, int version) {
            super(context, DATABASE_NAME, factory, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            String CREATE_PRODUCTS_TABLE = "CREATE TABLE " +
                    TABLE_PRODUCTS + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY," + COLUMN_PRODUCTNAME
                    + " TEXT," + COLUMN_QUANTITY + " INTEGER" + ")";
            db.execSQL(CREATE_PRODUCTS_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
            onCreate(db);
        }

        public void addProduct(Product product) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_PRODUCTNAME, product.getProductName());
            values.put(COLUMN_QUANTITY, product.getQuantity());

            SQLiteDatabase db = this.getWritableDatabase();

            db.insert(TABLE_PRODUCTS, null, values);
            db.close();
        }

        public Product findProduct(String productname) {
            String query = "SELECT * FROM " + TABLE_PRODUCTS + " WHERE " +
                    COLUMN_PRODUCTNAME + " = \"" + productname + "\"";

            SQLiteDatabase db = this.getWritableDatabase();

            Cursor cursor = db.rawQuery(query, null);

            Product product = new Product();

            if (cursor.moveToFirst()) {
                cursor.moveToFirst();
                product.setID(Integer.parseInt(cursor.getString(0)));
                product.setProductName(cursor.getString(1));
                product.setQuantity(Integer.parseInt(cursor.getString(2)));
                cursor.close();
            } else {
                product = null;
            }
            db.close();
            return product;
        }

        public boolean deleteProduct(String productname) {
            boolean result = false;
            String query = "SELECT * FROM " + TABLE_PRODUCTS + " WHERE " +
                    COLUMN_PRODUCTNAME + " = \"" + productname + "\"";

            SQLiteDatabase db = this.getWritableDatabase();

            Cursor cursor = db.rawQuery(query, null);

            Product product = new Product();

            if (cursor.moveToFirst()) {
                product.setID(Integer.parseInt(cursor.getString(0)));
                db.delete(TABLE_PRODUCTS, COLUMN_ID + " = ?",
                        new String[] { String.valueOf(product.getID()) });
                cursor.close();
                result = true;
            }
            db.close();

            return result;
        }
    }


    public class Product {

        private int _id;
        private String _productname;
        private int _quantity;

        public Product() {
        }

        public Product(int id, String productname, int quantity) {
            this._id = id;
            this._productname = productname;
            this._quantity = quantity;
        }

        public Product(String productname, int quantity) {
            this._productname = productname;
            this._quantity = quantity;
        }

        public void setID(int id) {
            this._id = id;
        }

        public int getID() {
            return this._id;
        }

        public void setProductName(String productname) {
            this._productname = productname;
        }

        public String getProductName() {
            return this._productname;
        }

        public void setQuantity(int quantity) {
            this._quantity = quantity;
        }

        public int getQuantity() {
            return this._quantity;
        }
    }

}

