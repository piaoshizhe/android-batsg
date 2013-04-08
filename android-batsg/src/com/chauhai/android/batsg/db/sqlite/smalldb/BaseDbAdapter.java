package com.chauhai.android.batsg.db.sqlite.smalldb;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.chauhai.android.batsg.db.sqlite.smalldb.BaseDbBean.CursorColumnIndex;
import com.chauhai.android.batsg.util.DebugUtil;
import com.chauhai.android.batsg.util.ErrorUtil;

/**
 * Base class for data base adapter, which manipulates accessing to a database table.
 * <p>
 * Generic A is the child class itself. Generic B is the bean class.
 * <p>
 * Example:
 * <pre>
 * class DTableName extends DbAdapter&lt;DTableName, BTableName&gt; {};
 * DTableName db = new DTableName(context);
 * BTableName bean = db.open().find(1);
 * db.close();
 * </pre>
 * Convention:
 * <p>
 * <ul>
 *   <li>All database table should has column <em>_id</em> as the primary key.</li>
 *   <li>
 *   For the database table name <em>table_name</em>,
 *   the db adapter class name should be DTableName,
 *   and the bean class name should be BTableName.
 *   </li>
 * </ul>
 *
 * @author umbalaconmeogia
 */
public abstract class BaseDbAdapter<A extends BaseDbAdapter<A, B>, B extends BaseDbBean>
    extends BaseDb {

  private static final String TAG = "BaseDbAdapter"; // Log tag.

  // Column names.
  public static final String COL_ID = "_id";

  private SQLiteOpenHelper dbOpenHelper;

  protected Context context;

  protected SQLiteDatabase database;

  /**
   * Create an instance of a DbAdapter class.
   * @param <A>
   * @param <B>
   * @param context
   * @param adapterClass
   * @return
   */
  public static <A extends BaseDbAdapter<A, B>, B extends BaseDbBean>
      A instantiate(Context context, Class<A> adapterClass) {

    try {
      Class<?>[] contextArgsClass = new Class<?>[] {Context.class}; // Parameter signature.
      Object[] contextArgs = new Object[] {context}; // Parameter value.
      Constructor<A> constructor = adapterClass.getConstructor(contextArgsClass);
      return constructor.newInstance(contextArgs);
    } catch (Exception e) {
      throw ErrorUtil.runtimeException(e);
    }
  }

  /**
   * Create a DbAdapter and open it.
   * @param <A>
   * @param <B>
   * @param context
   * @param dbAdapterClass
   * @return The DbAdapter instance.
   */
  public static <A extends BaseDbAdapter<A, B>, B extends BaseDbBean>
      A open(Context context, Class<A> dbAdapterClass) {

    return instantiate(context, dbAdapterClass).open();
  }

  /**
   * Create the db adapter object.
   * <p>
   * WARNING: Create DbAdapter object does not mean opening the database.
   * The open() method should be called before executing any SQL command.
   * @param context
   */
  public BaseDbAdapter(Context context) {
    this.context = context;
  }

  /**
   * Create an DbOpenHelper object.
   * <p>
   * Subclass should declare this method, returning a DbOpenHelper object.
   * For example:
   * <pre>
   * public DbOpenHelper createDbOpenHelper() {
   *   return DbOpenHelper(context);
   * }
   * </pre>
   * @return
   */
  protected abstract SQLiteOpenHelper createDbOpenHelper();

  /**
   * Get the DbBeanClassFactory object.
   * @return
   */
  protected abstract BaseDbBeanClassFactory dbBeanClassFactory();

  /**
   * Store the bean class type.
   */
  private Class<B> beanClass;

  /**
   * Get Bean class appropriate with this adapter class.
   * <p>
   * The bean class name is this class name with the first character replaced by "B".
   * <p>
   * Subclass may override this for faster implementation. For example:
   * <pre>
   * protected Class&lt;BTableName&gt; beanClass() {
   *   return BTableName.class;
   * }
   * </pre>
   * @return
   */
  @SuppressWarnings("unchecked")
  protected Class<B> beanClass() {
    if (beanClass == null) {
      String beanClassName = "B" + className().substring(1);
      beanClass = (Class<B>) dbBeanClassFactory().beanClassBySimpleName(beanClassName);
    }
    return beanClass;
  }

  /**
   * Create a bean from a cursor.
   * <p>
   * Subclass may override this for a faster implementation. For example:
   * <pre>
   * public BTableName parseBean(Cursor cursor) {
   *   return BaseDbBean.parse(cursor, BTableName.class, null);
   * }
   * </pre>
   * @param cursor
   * @return
   */
  protected B parseBean(Cursor cursor) {
    return BaseDbBean.parse(cursor, beanClass(), null);
  }

  /**
   * Create a bean from a cursor.
   * <p>
   * Subclass may override this for a faster implementation. For example:
   * <pre>
   * public BTableName parseBean(Cursor cursor, cursorColumnIndex) {
   *   return BaseDbBean.parse(cursor, BTableName.class, cursorColumnIndex);
   * }
   * </pre>
   * @param cursor
   * @param cursorColumnIndex
   * @return
   */
  protected B parseBean(Cursor cursor, CursorColumnIndex cursorColumnIndex) {
    return BaseDbBean.parse(cursor, beanClass(), cursorColumnIndex);
  }

  /**
   * Open the database.
   * @return
   * @throws SQLException
   */
  @SuppressWarnings("unchecked")
  public A open() throws SQLException {
    Log.d(TAG, "open()");
    if (dbOpenHelper == null) {
      dbOpenHelper = createDbOpenHelper();
    }
    database = dbOpenHelper.getWritableDatabase();
    return (A) this;
  }

  /**
   * Close the database.
   */
  public void close() {
    dbOpenHelper.close();
  }

  /**
   * Wrapper for {@link SQLiteDatabase#execSQL(String)}.
   * @param sql
   */
  public void execSQL(String sql) {
    Log.d(TAG, "sql: " + sql);
    database.execSQL(sql);
  }

  /**
   * Wrapper for {@link SQLiteDatabase#execSQL(String, Object[])
   * @param sql
   * @param bindArgs
   */
  public void execSQL(String sql, Object[] bindArgs) {
    Log.d(TAG, "execSQL(" + sql + ", " + DebugUtil.arrayToString(bindArgs) + ")");
    database.execSQL(sql, bindArgs);
  }

  /**
   * Get the first record by SQL statement.
   * @param sql
   * @param selectionArgs
   * @return
   */
  public B find(String sql, String[] selectionArgs) {
    Log.d(TAG, "sql: " + sql);
    Cursor cursor = database.rawQuery(sql, selectionArgs);
    // Parse result.
    B bean = null;
    if (cursor != null) {
      cursor.moveToFirst();
      if (!cursor.isAfterLast()) {
        bean = parseBean(cursor);
      }
      cursor.close();
    }
    return bean;
  }

  /**
   * Get the first record by SQL statement.
   * @param <A>
   * @param <B>
   * @param context
   * @param dbAdapterClass
   * @param sql
   * @param selectionArgs
   * @return
   */
  public static <A extends BaseDbAdapter<A, B>, B extends BaseDbBean>
      B find(Context context, Class<A> dbAdapterClass, String sql, String[] selectionArgs) {

    A db = open(context, dbAdapterClass);
    B result = db.find(sql, selectionArgs);
    db.close();
    return result;
  }

  /**
   * Get the first record by SQL statement.
   * @param sql
   * @return
   */
  public B find(String sql) {
    return find(sql, null);
  }

  /**
   * Get the first record by SQL statement.
   * @param <A>
   * @param <B>
   * @param context
   * @param dbAdapterClass
   * @param sql
   * @return
   */
  public static <A extends BaseDbAdapter<A, B>, B extends BaseDbBean>
      B find(Context context, Class<A> dbAdapterClass, String sql) {

    return find(context, dbAdapterClass, sql, null);
  }

  /**
   * Get record specified by id.
   * @param id Record id.
   * @return
   */
  public B find(int id) {
    String sql = "SELECT * FROM " + tableName() + " WHERE " + COL_ID + " = " + id; // SQL statement.
    return find(sql);
  }

  /**
   * Get record specified by id.
   * @param <A>
   * @param <B>
   * @param context
   * @param dbAdapterClass
   * @param id
   * @return
   */
  public static <A extends BaseDbAdapter<A, B>, B extends BaseDbBean>
      B find(Context context, Class<A> dbAdapterClass, int id) {

    A db = open(context, dbAdapterClass);
    B result = db.find(id);
    db.close();
    return result;
  }

  /**
   * Get the first record by SQL statement with specified WHERE clause.
   * @param whereClause Where clause (without keyword WHERE).
   * @param selectionArgs
   * @return
   */
  public B findWhere(String whereClause, String[] selectionArgs) {
    String sql = "SELECT * FROM " + tableName() + " WHERE " + whereClause;
    return find(sql, selectionArgs);
  }

  /**
   * Get the first record by SQL statement with specified WHERE clause.
   * @param <A>
   * @param <B>
   * @param context
   * @param dbAdapterClass
   * @param whereClause Where clause (without keyword WHERE).
   * @param selectionArgs
   * @return
   */
  public static <A extends BaseDbAdapter<A, B>, B extends BaseDbBean>
      B findWhere(Context context, Class<A> dbAdapterClass, String whereClause, String[] selectionArgs) {

    A db = open(context, dbAdapterClass);
    B result = db.findWhere(whereClause, selectionArgs);
    db.close();
    return result;
  }

  /**
   * Get the first record by SQL statement with specified WHERE clause.
   * @param <A>
   * @param <B>
   * @param context
   * @param dbAdapterClass
   * @param whereClause Where clause (without keyword WHERE).
   * @return
   */
  public static <A extends BaseDbAdapter<A, B>, B extends BaseDbBean>
      B findWhere(Context context, Class<A> dbAdapterClass, String whereClause) {

    return findWhere(context, dbAdapterClass, whereClause, null);
  }

  /**
   * Get all records by SQL statement.
   * @param sql
   * @param selectionArgs
   * @return
   */
  public List<B> findAll(String sql, String[] selectionArgs) {
    Log.d(TAG, "findAll(" + sql + ", " + DebugUtil.arrayToString(selectionArgs) + ")");
    List<B> result = new ArrayList<B>(); // Return value.
    Log.d(TAG, "database: " + database);
    Cursor cursor = database.rawQuery(sql, selectionArgs);
    // Parse result.
    if (cursor != null) {
      cursor.moveToFirst();
      CursorColumnIndex cursorColumnIndex = new CursorColumnIndex(cursor);
      while (!cursor.isAfterLast()) {
        // Parse db cursor.
        B bean = parseBean(cursor, cursorColumnIndex);
        // Add to list.
        result.add(bean);
        cursor.moveToNext();
      }
      cursor.close();
    }
    return result;
  }

  /**
   * Get all records by SQL statement.
   * @param <A>
   * @param <B>
   * @param context
   * @param dbAdapterClass
   * @param sql
   * @param selectionArgs
   * @return
   */
  public static <A extends BaseDbAdapter<A, B>, B extends BaseDbBean>
      List<B> findAll(Context context, Class<A> dbAdapterClass, String sql, String[] selectionArgs) {

    A db = open(context, dbAdapterClass);
    List<B> result = db.findAll(sql, selectionArgs);
    db.close();
    return result;
  }

  /**
   * Get all records by SQL statement.
   * @param sql
   * @return
   */
  public List<B> findAll(String sql) {
    return findAll(sql, null);
  }

  /**
   * Get all records by SQL statement.
   * @param <A>
   * @param <B>
   * @param context
   * @param dbAdapterClass
   * @param sql
   * @return
   */
  public static <A extends BaseDbAdapter<A, B>, B extends BaseDbBean>
      List<B> findAll(Context context, Class<A> dbAdapterClass, String sql) {

    return findAll(context, dbAdapterClass, sql, null);
  }

  /**
   * Get all records.
   * @return The list of records ordered by id.
   */
  public List<B> findAll() {
    String sql = "SELECT * FROM " + tableName() + " ORDER BY " + COL_ID; // SQL statement.
    return findAll(sql, null);
  }

  /**
   * Get all records.
   * @param <A>
   * @param <B>
   * @param context
   * @param dbAdapterClass
   * @return
   */
  public static <A extends BaseDbAdapter<A, B>, B extends BaseDbBean>
      List<B> findAll(Context context, Class<A> dbAdapterClass) {

    A db = open(context, dbAdapterClass);
    List<B> result = db.findAll();
    db.close();
    return result;
  }

  /**
   * Get all records by SQL statement with specified WHERE clause.
   * @param whereClause Where clause (without keyword WHERE).
   * @param selectionArgs
   * @return
   */
  public List<B> findAllWhere(String whereClause, String[] selectionArgs) {
    String sql = "SELECT * FROM " + tableName() + " WHERE " + whereClause;
    return findAll(sql, selectionArgs);
  }

  /**
   * Get all records by SQL statement with specified WHERE clause.
   * @param <A>
   * @param <B>
   * @param context
   * @param dbAdapterClass
   * @param whereClause Where clause (without keyword WHERE).
   * @param selectionArgs
   * @return
   */
  public static <A extends BaseDbAdapter<A, B>, B extends BaseDbBean>
      List<B> findAllWhere(Context context, Class<A> dbAdapterClass, String whereClause, String[] selectionArgs) {

    A db = open(context, dbAdapterClass);
    List<B> result = db.findAllWhere(whereClause, selectionArgs);
    db.close();
    return result;
  }

  /**
   * Get all records by SQL statement with specified WHERE clause.
   * @param <A>
   * @param <B>
   * @param context
   * @param dbAdapterClass
   * @param whereClause Where clause (without keyword WHERE).
   * @return
   */
  public static <A extends BaseDbAdapter<A, B>, B extends BaseDbBean>
      List<B> findAllWhere(Context context, Class<A> dbAdapterClass, String whereClause) {

    return findAllWhere(context, dbAdapterClass, whereClause, null);
  }

  /**
   * Get all records by SQL statement, map them by record id.
   * @param sql
   * @param selectionArgs
   * @return
   */
  public Map<Integer, B> map(String sql, String[] selectionArgs) {
    Map<Integer, B> result = new HashMap<Integer, B>(); // Return value.
    List<B> list = findAll(sql, selectionArgs);
    for (B bean: list) {
      result.put(bean._id, bean);
    }
    return result;
  }

  /**
   * Get all records by SQL statement, map them by record id.
   * @param <A>
   * @param <B>
   * @param context
   * @param dbAdapterClass
   * @param sql
   * @param selectionArgs
   * @return
   */
  public static <A extends BaseDbAdapter<A, B>, B extends BaseDbBean>
      Map<Integer, B> map(Context context, Class<A> dbAdapterClass, String sql, String[] selectionArgs) {

    A db = open(context, dbAdapterClass);
    Map<Integer, B> result = db.map(sql, selectionArgs);
    db.close();
    return result;
  }

  /**
   * Get all records by SQL statement, map them by record id.
   * @param sql
   * @return
   */
  public Map<Integer, B> map(String sql) {
    return map(sql, null);
  }

  /**
   * Get all records by SQL statement, map them by record id.
   * @param <A>
   * @param <B>
   * @param context
   * @param dbAdapterClass
   * @param sql
   * @return
   */
  public static <A extends BaseDbAdapter<A, B>, B extends BaseDbBean>
      Map<Integer, B> map(Context context, Class<A> dbAdapterClass, String sql) {

    return map(context, dbAdapterClass, sql, null);
  }

  /**
   * Get all records, map them by record id.
   * @return
   */
  public Map<Integer, B> map() {
    String sql = "SELECT * FROM " + tableName(); // SQL statement.
    return map(sql, null);
  }

  /**
   * Get all records, map them by record id.
   * @param <A>
   * @param <B>
   * @param context
   * @param dbAdapterClass
   * @return
   */
  public static <A extends BaseDbAdapter<A, B>, B extends BaseDbBean>
      Map<Integer, B> map(Context context, Class<A> dbAdapterClass) {

    A db = open(context, dbAdapterClass);
    Map<Integer, B> result = db.map();
    db.close();
    return result;
  }

  /**
   * Get all records by SQL statement with specified WHERE clause, map them by record id.
   * @param whereClause
   * @param selectionArgs
   * @return
   */
  public Map<Integer, B> mapWhere(String whereClause, String[] selectionArgs) {
    String sql = "SELECT * FROM " + tableName() + " WHERE " + whereClause;
    return map(sql, selectionArgs);
  }

  /**
   * Get all records by SQL statement with specified WHERE clause, map them by record id.
   * @param <A>
   * @param <B>
   * @param context
   * @param dbAdapterClass
   * @param whereClause
   * @param selectionArgs
   * @return
   */
  public static <A extends BaseDbAdapter<A, B>, B extends BaseDbBean>
      Map<Integer, B> mapWhere(Context context, Class<A> dbAdapterClass, String whereClause, String[] selectionArgs) {

    A db = open(context, dbAdapterClass);
    Map<Integer, B> result = db.mapWhere(whereClause, selectionArgs);
    db.close();
    return result;
  }

  /**
   * Get all records by SQL statement with specified WHERE clause, map them by record id.
   * @param <A>
   * @param <B>
   * @param context
   * @param dbAdapterClass
   * @param whereClause
   * @return
   */
  public static <A extends BaseDbAdapter<A, B>, B extends BaseDbBean>
      Map<Integer, B> mapWhere(Context context, Class<A> dbAdapterClass, String whereClause) {

    return mapWhere(context, dbAdapterClass, whereClause, null);
  }

  /**
   * Update rows in the database.
   * <p>
   * This is the wrapper for SQLiteDatabase.update(), with Bean to be converted to ContentValues.
   * @param bean Update field/values. This will be converted to ContentValues.
   * @param whereClause
   * @param whereArgs
   * @return the number of rows affected.
   */
  public int update(B bean, String whereClause, String[] whereArgs) {
    return database.update(tableName(), bean.toContentValues(), whereClause, whereArgs);
  }

  /**
   * Update rows in the database.
   * <p>
   * This is the wrapper for SQLiteDatabase.update(), with Bean to be converted to ContentValues.
   * @param <A>
   * @param <B>
   * @param context
   * @param dbAdapterClass
   * @param bean Update field/values. This will be converted to ContentValues.
   * @param whereClause
   * @param whereArgs
   * @return the number of rows affected.
   */
  public static <A extends BaseDbAdapter<A, B>, B extends BaseDbBean>
      int update(Context context, Class<A> dbAdapterClass, B bean, String whereClause, String[] whereArgs) {

    A db = open(context, dbAdapterClass);
    int result = db.update(bean, whereClause, whereArgs);
    db.close();
    return result;
  }

  /**
   * Update data of a record (specified by its id).
   * @param bean contains the data to be updated and also the id of the record to be updated.
   * @return
   */
  public int update(B bean) {
    return database.update(tableName(), bean.toContentValues(), COL_ID + " = " + bean._id, null);
  }

  /**
   * Update data of a record (specified by its id).
   * @param <A>
   * @param <B>
   * @param context
   * @param dbAdapterClass
   * @param bean contains the data to be updated and also the id of the record to be updated.
   * @return
   */
  public static <A extends BaseDbAdapter<A, B>, B extends BaseDbBean>
      int update(Context context, Class<A> dbAdapterClass, B bean) {

    A db = open(context, dbAdapterClass);
    int result = db.update(bean);
    db.close();
    return result;
  }

  /**
   * Update rows in the database.
   * <p>
   * Notice: This use {@link SQLiteDatabase#execSQL(String, Object[])} to run the UPDATW statement,
   * so it does not the number of rows affected.
   * @param setClause
   * @param whereClause
   * @param bindArgs
   */
  public void update(String setClause, String whereClause, Object[] bindArgs) {
    String sql = "UPDATE " + tableName() + " SET " + setClause;
    if (whereClause != null) {
      sql += " WHERE " + whereClause;
    }
    execSQL(sql, bindArgs);
  }

  /**
   * Update rows in the database.
   * <p>
   * Notice: This use {@link SQLiteDatabase#execSQL(String, Object[])} to run the UPDATW statement,
   * so it does not the number of rows affected.
   * @param <A>
   * @param <B>
   * @param context
   * @param dbAdapterClass
   * @param setClause
   * @param whereClause
   * @param bindArgs
   */
  public static <A extends BaseDbAdapter<A, B>, B extends BaseDbBean>
    void update(Context context, Class<A> dbAdapterClass, String setClause, String whereClause, Object[] bindArgs) {

    A db = open(context, dbAdapterClass);
    db.update(setClause, whereClause, bindArgs);
    db.close();
  }
}