package org.anyline.data.mongodb;


import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.lang.Nullable;


/**
 * MongoDB 操作类
 * @author hadoop
 *
 */
public class MongodbHelper {

    private MongoDatabase db;
    private MongodbConn conn;

    public MongodbHelper() {
        //获取数据库对象
        if(db==null || conn ==null) {
            conn = new MongodbConn();
            db = conn.getDatabase();
        }
    }
    /**
     * 关闭连接对象
     */
    public void closeDb() {
        if(db!=null) {
            conn.close();
        }
    }
    /**
     * //获取指定文档集合对象
     * @param coll
     * @return
     */
    public MongoCollection<Document> getColl(String coll) {
        MongoCollection<Document> mongocoll = db.getCollection(coll);
        return mongocoll;
    }
    /**
     * 保存一条数据
     * @param coll,表名
     * @param map
     */
    public void insertOne(String coll, Map<String,Object> map) {
        //获取文档集合对象
        MongoCollection<Document> doc = getColl(coll);
        //将map转换为Document
        Document document = new Document(map);
        //插入文档
        doc.insertOne(document);
    }

    /**
     * 保存一条数据
     * @param coll,表名
     * @param json JSON对象字符串数据
     */
    public void insertOne(String coll,String json) {
        //获取文档集合对象
        MongoCollection<Document> doc = getColl(coll);
        //将map转换为Document
        Document document = Document.parse(json);
        //插入文档
        doc.insertOne(document);
    }

    /**
     * 将一个对象插入到一个数据源。
     * @param obj 要被插入的对象
     *             <p>
     *            它可以是：
     *            <ul>
     *            <li>普通 POJO
     *            <li>List集合
     *            </ul>
     *            <b style=color:red>注意：</b> 如果是list，所有的对象必须类型相同，否则可能会出错
     *
     */
    public boolean insert(Object obj) {
        if(obj==null) {
            return false;
        }
       if(obj instanceof ArrayList || obj instanceof LinkedList) {
            Object firstObj = first(obj);
            List<Document> list = new ArrayList<Document>();
            Collection parseObj = (Collection) obj;
            parseObj.forEach(item->{
                Document doc = null;//Document.parse(JSON.toJSONString(item));
                list.add(doc);
            });
            insertMany(getClazzName(firstObj), list);
        }else{
            //获取文档集合对象
            MongoCollection<Document> doc = getColl(getClazzName(obj));
            //将map转换为Document
            Document document = Document.parse("{name:'zh123'}");
            //插入文档
            doc.insertOne(document);
        }
        return true;
    }

    /**
     * 保存多条数据
     * @param coll 表名
     * @param list 多个Document集合
     */
    public void insertMany(String coll,List<Document> list) {
        //获取文档集合对象
        MongoCollection<Document> doc = getColl(coll);
        //插入文档
        doc.insertMany(list);
    }
    /**
     * 删除单条数据
     * @param coll 表名
     * @param params 筛选条件对象
     * @return
     */
    public long deleteById(String coll,BasicDBObject params) {
        //获取文档集合对象
        MongoCollection<Document> doc = getColl(coll);
        //删除单条数据
        DeleteResult result = doc.deleteOne(params);
        return result.getDeletedCount();
    }
    /**
     * 删除单条数据
     @param clazz 对象类型
     @param params 筛选条件对象
     @return
     */
    public long deleteById(Class<?> clazz,BasicDBObject params) {
        return deleteById(getClazzName(clazz),params);
    }
    /**
     * 删除单条数据
     @param clazz 对象类型
     @param key 删除的主键名
     @param value 删除的条件值
     @return
     */
    public long deleteById(Class<?> clazz,String key,Object value) {
        if(clazz==null || key==null || value==null) {
            return -1;
        }
        //设置条件参数
        BasicDBObject params = new BasicDBObject(key, value);
        return deleteById(getClazzName(clazz),params);
    }

    /**
     * 删除多条数据
     * @param coll 表名
     * @param params 筛选条件对象
     * @return
     */
    public long delete(String coll,BasicDBObject params) {
        //获取文档集合对象
        MongoCollection<Document> doc = getColl(coll);
        //删除多条数据
        DeleteResult result = doc.deleteMany(params);
        return result.getDeletedCount();
    }
    /**
     * 查询所有数据
     * @param coll 表名
     * @return
     */
    public FindIterable<Document> query(String coll) {
        //获取文档集合对象
        MongoCollection<Document> doc = getColl(coll);
        return doc.find();
    }

    public <T> FindIterable<Document> query(Class<T> clazz) {
        //获取文档集合对象
        MongoCollection<Document> doc = getColl(getClazzName(clazz));
        return doc.find();
    }

    /**
     * 根据条件查询数据
     * @param coll 表名
     * @param params 查询条件
     * @return
     */
    public FindIterable<Document> query(String coll,BasicDBObject params) {
        //获取文档集合对象
        MongoCollection<Document> doc = getColl(coll);
        return doc.find(params);
    }
    public FindIterable<Document> query(Class<?> clazz,Bson filter) {
        //获取文档集合对象
        MongoCollection<Document> doc = getColl(getClazzName(clazz));
        return doc.find(filter);
    }
    /**
     * 根据主键_id查找对象
     @param coll
     @param id
     @return
     */
    public Document findById(String coll, String id) {
        ObjectId _idobj = null;
        try {
            _idobj = new ObjectId(id);
        } catch (Exception e) {
            return null;
        }
        //获取文档集合对象
        MongoCollection<Document> doc = getColl(coll);
        Document myDoc = doc.find(Filters.eq("_id", _idobj)).first();
        return myDoc;
    }
    /**
     * 分页查询
     @param coll
     @param params
     @param page
     @param limit
     @return
     */
    public MongoCursor<Document> queryByPage(String coll,BasicDBObject params, int page, int limit) {
        MongoCollection<Document> doc = getColl(coll);
        //排序
        Bson orderBy = new BasicDBObject("_id", 1);
        FindIterable<Document> docfilter =  params==null?doc.find():doc.find(params);
        return docfilter.sort(orderBy).skip((page - 1) * page).limit(limit).iterator();
    }
    /**
     *
     @param clazz 对象类型
     @param filter 过滤条件，如：Filters.eq("name", "张三")
     @param page
     @param limit
     @return
     */
    public <T> List<T> queryByPage(Class<T> clazz,Bson filter, int page, int limit) {
        MongoCollection<Document> doc = getColl(getClazzName(clazz));
        //排序
        Bson orderBy = new BasicDBObject("_id", 1);
        FindIterable<Document> docfilter =  filter==null?doc.find():doc.find(filter);
        MongoCursor<Document> result = docfilter.sort(orderBy).skip((page - 1) * page).limit(limit).iterator();
        List<T> list = new ArrayList<T>();
        while(result.hasNext()) {
           // JSONObject json = JSON.parseObject(result.next().toJson());
           // json.remove("_id");
           // T item = JSON.toJavaObject(json, clazz);
           // list.add(item);
        }
        return list;
    }
    /**
     *
     @param clazz 对象类型
     @param filter 过滤条件，如：filter = Filters.eq("name", "张三")
     @param orderBy 排序，如：orderBy = new BasicDBObject("date", 1)；1升序，－1表示倒序
     @param page
     @param limit
     @return
     */
    public <T> List<T> queryByPage(Class<T> clazz,Bson filter,Bson orderBy, int page, int limit) {
        MongoCollection<Document> doc = getColl(getClazzName(clazz));
        //排序
        if(orderBy==null) {
            orderBy = new BasicDBObject("_id", 1);
        }
        FindIterable<Document> docfilter =  filter==null?doc.find():doc.find(filter);
        MongoCursor<Document> result = docfilter.sort(orderBy).skip((page - 1) * page).limit(limit).iterator();
        List<T> list = new ArrayList<T>();
        while(result.hasNext()) {
           // JSONObject json = JSON.parseObject(result.next().toJson());
           // json.remove("_id");
           // T item = JSON.toJavaObject(json, clazz);
          //  list.add(item);
        }
        return list;
    }
    /**
     * 查询所有数据
     @param clazz 对象类型
     @param filter 过滤条件，如：Filters.eq("name", "张三")
     @return
     */
    @SuppressWarnings("hiding")
    public <T> List<T> queryAllList(Class<T> clazz,Bson filter) {
        MongoCollection<Document> doc = getColl(getClazzName(clazz));
        //排序
        Bson orderBy = new BasicDBObject("_id", 1);
        FindIterable<Document> docfilter =  filter==null?doc.find():doc.find(filter);
        MongoCursor<Document> result = docfilter.sort(orderBy).iterator();
        List<T> list = new ArrayList<T>();
        while(result.hasNext()) {
         //   JSONObject json = JSON.parseObject(result.next().toJson());
          //  json.remove("_id");
         //   T item = JSON.toJavaObject(json, clazz);
         //   list.add(item);
        }
        return list;
    }
    /**
     *  统计总数
     *  @param coll 集合名
     */
    public long count(String coll) {
        //获取文档集合对象
        MongoCollection<Document> doc = getColl(coll);
        return doc.countDocuments();
    }
    /**
     * 统计总数
     @param clazz JavaBean类
     @return
     */
    public long count(Class<?> clazz) {
        //获取文档集合对象
        MongoCollection<Document> doc = getColl(getClazzName(clazz));
        return doc.countDocuments();
    }
    /**
     * 根据条件查询数量
     @param coll
     @param params
     @return
     */
    public long count(String coll,BasicDBObject params) {
        //获取文档集合对象
        MongoCollection<Document> doc = getColl(coll);
        return doc.countDocuments(params);
    }
    /**
     * 根据条件查询数量
     @param clazz JavaBean类
     @param params 参数
     @return
     */
    public long count(Class<?> clazz,BasicDBObject params) {
        //获取文档集合对象
        MongoCollection<Document> doc = getColl(getClazzName(clazz));
        return doc.countDocuments(params);
    }
    /**
     * 修改一条数据
     * @param coll 表名
     * @param params 参数(类似于sql中的where条件)
     * @param document 需要操作的文档对象
     * <pre>
     *  //设置条件参数
     * BasicDBObject params = new BasicDBObject("id", 5);
     *  //实例化修改对象：
     * Document document = new Document();
     * //添加修改内容：
     * document.append("$set",new Document("sex","男"));
     * </pre>
     */
    public void update(String coll,BasicDBObject params,Document document) {
        //获取文档集合对象
        MongoCollection<Document> doc = getColl(coll);
        doc.updateOne(params,document);
    }
    /**
     * 修改一条数据
     *@param clazz JavaBean类
     * @param params 参数(类似于sql中的where条件)
     * @param document 需要操作的文档对象
     * <pre>
     * //设置条件参数
     * BasicDBObject params = new BasicDBObject("id", 5);
     *  //实例化修改对象：
     * Document document = new Document();
     * //添加修改内容：
     * document.append("$set",new Document("sex","男"));
     * </pre>
     */
    public void update(Class<?> clazz,BasicDBObject params,Document document) {
        //获取文档集合对象
        MongoCollection<Document> doc = getColl(getClazzName(clazz));
        doc.updateOne(params,document);
    }

    /**
     * 修改多条数据
     * @param coll 表名
     * @param params 参数(类似于sql中的where条件)
     * @param document 需要操作的文档对象
     * <pre>
     * 实例化修改对象：
     * Document document = new Document();
     * //添加修改内容：
     * document.append("$set",new Document("sex","男"));
     * </pre>
     */
    public void updateMany(String coll,BasicDBObject params,Document document) {
        //获取文档集合对象
        MongoCollection<Document> doc = getColl(coll);
        doc.updateMany(params,document);
    }
    /**
     * 设置mongodb修改数据，通过反射获取javaBean的属性名和值
     @param obj 数据对象
     @param document 修改对象
     @param filterField 需要过滤的字段，格式为javaBean实体属性名,多个用竖线|分隔，如：id|userPasswd
     @return
     */
    public Document updateSetFieldVal(Object obj,Document document,String filterField) {
        if(obj==null) {return document;}
        try {
            //构造修改数据对象，mongodb修改数据命令为：db.news.update({'id':11},{$set:{'name':'111',"title":"标题"}},{multi:true})
            Document doc = new Document();
            for (Field field : obj.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                Object value = field.get(obj);
                Nullable nb = field.getAnnotation(Nullable.class);
                //判断需要过滤的属性
                if(filterField!=null && !"".equals(filterField)) {
                    String[] filterFields = filterField.split("\\|");
                    if(Arrays.asList(filterFields).contains(field.getName())) {
                        continue;
                    }
                }
                if(nb !=null) {
                    doc.append(field.getName(),value);
                }else {
                    if (value!=null) {
                        doc.append(field.getName(),value);
                    }
                }
            }
            document.append("$set",doc);
            return document;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("对象属性为空异常" + e);
            return document;
        }
    }
    /**
     *获取指定对象的类名，并将首字母转为小写
     */
    private String getClazzName(Object obj) {
        String clazzName = obj.getClass().getSimpleName();
        String firstChar=clazzName.substring(0,1);
        clazzName=clazzName.replaceFirst(firstChar, firstChar.toLowerCase());
        return clazzName;
    }
    /**
     *获取指定类的类名，并将首字母转为小写
     */
    private String getClazzName(Class<?> clazz) {
        String clazzName = clazz.getSimpleName();
        String firstChar=clazzName.substring(0,1);
        clazzName=clazzName.replaceFirst(firstChar, firstChar.toLowerCase());
        return clazzName;
    }
    /**
     *获取集合的第一个元素
     */
    public static Object first(Object obj) {
        if (null == obj)
            return obj;

        if (obj instanceof Collection<?>) {
            Iterator<?> it = ((Collection<?>) obj).iterator();
            return it.hasNext() ? it.next() : null;
        }

        if (obj.getClass().isArray())
            return Array.getLength(obj) > 0 ? Array.get(obj, 0) : null;

        return obj;
    }

    public static void main(String[] args) {
        MongodbHelper mongodbHelper = new MongodbHelper();
        Map<String,Object> map = new HashMap<>();
        map.put("id",100);
        map.put("name","张三");
        map.put("age",18);
        map.put("stuNo", "2106050540000");
        map.put("sex","女");
        mongodbHelper.insertOne("userInfo", map);

        FindIterable<Document> list = mongodbHelper.query("userInfo");
        for(Document doc:list) {
            System.out.println(doc);
        }
        DemoData demoData = new DemoData();
        demoData.setId(UUID.randomUUID().toString().replace("-", ""));
        demoData.setCode("200");
        demoData.setDate(LocalDateTime.now().toString());
        demoData.setName("李四");
        demoData.setDoubleData(2022);
        //增加
        mongodbHelper.insert(demoData);

        //修改开始///
        //实例化修改对象：
        Document document = new Document();
        //添加修改内容：
        mongodbHelper.updateSetFieldVal(demoData, document, "id");
        //执行修改
        BasicDBObject params = new BasicDBObject("id", "682afe866bbf43e9bf4a62c8f3cdda7c");
        mongodbHelper.update(DemoData.class, params, document);
        //修改结束///

        //查询
        Bson param = Filters.eq("name", "张三");
        List<DemoData> rslist = mongodbHelper.queryByPage(DemoData.class, param,1,5);
        System.out.println(rslist);
    }

}


