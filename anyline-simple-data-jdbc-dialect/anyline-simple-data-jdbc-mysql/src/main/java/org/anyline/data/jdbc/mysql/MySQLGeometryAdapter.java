package org.anyline.data.jdbc.mysql;

import org.anyline.entity.geometry.*;
import org.anyline.util.NumberUtil;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class MySQLGeometryAdapter {

    private static Map<Integer, Geometry.Type> types = new Hashtable<>();
    static {
        types.put(1, Geometry.Type.Point);
        types.put(2, Geometry.Type.Line);
        types.put(3, Geometry.Type.Polygon);
        types.put(4, Geometry.Type.MultiPoint);
        types.put(5, Geometry.Type.MultiLine);
        types.put(6, Geometry.Type.MultiPolygon);
        types.put(7, Geometry.Type.GeometryCollection);
    }
    public static Geometry.Type type(Integer type){
        return types.get(type);
    }
    public static byte[] bytes(Geometry geometry){
        return null;
    }
    public static String sql(Geometry geometry){
        return null;
    }
    public static byte[] bytes(Point point){
        byte[] bx= NumberUtil.double2bytes(point.getX());
        byte[] by= NumberUtil.double2bytes(point.getY());
        byte[] bytes =new byte[25];
        bytes[4]=0x01;
        bytes[5]=0x01;
        for(int i=0;i<8;++i){
            bytes[9+i]=bx[i];
            bytes[17+i]=by[i];
        }
        return bytes;
    }
    public static String sql(Point point){
        return "Point(" + point.getX() + " " + point.getY() + ")";
    }
    public static Geometry parse(byte[] bytes){
        Geometry geometry = null;
        //取字节数组的前4个来解析srid
        byte[] srid_bytes = new byte[4];
        System.arraycopy(bytes, 0, srid_bytes, 0, 4);
        //是否大端格式
        boolean bigEndian = (bytes[4] == 0x00);
        // 解析SRID
        int srid = NumberUtil.byte2int(bytes, 0, 4, bigEndian);
        int type = NumberUtil.byte2int(bytes, 5, 4, bigEndian);
        if(type == 1){
            geometry = parsePoint(bytes);
        }else if(type == 2){
            geometry = parseLine(bytes);
        }else if(type == 3){
            geometry = parsePolygon(bytes);
        }
        geometry.setSrid(srid);
        return geometry;
    }
    /*
        POINT(120 36.1)
        bytes[25]:
        00 00 00 00, 01, 01 00 00 00, 00 00 00 00 00 00 5e 40, cd cc cc cc cc 0c 42 40
        component	    size(起-止) decimal hex
        SRID            4(0-3)      0       00 00 00 00
        Byte order	    1(4-4)  	1       01(1:小端,0:大端)
        WKB type	    4(5-8)  	1       01 00 00 00
        X(经度)	        8(9-16) 	120.0   00 00 00 00 00 00 5e 40
        Y(纬度)	        8(17-24)	36.1    cd cc cc cc cc 0c 42 40
    */

    /**
     * 解析 Point
     * @param bytes bytes
     * @return Point
     */
    public static Point parsePoint(byte[] bytes){
        double x = NumberUtil.byte2double(bytes, 9);
        double y = NumberUtil.byte2double(bytes, 17);
        Point point = new Point(x, y);
        return point;
    }

    /*
        LINESTRING(1 2, 15 15, 11 22)
        bytes[61]:
        00 00 00 00, 01, 02 00 00 00 ,03 00 00 00, 00 00 00 00 00 00 f0 3f, 00 00 00 00 00 00 00 40, 00 00 00 00 00 00 2e 40, 00 00 00 00 00 00 2e 40, 00 00 00 00 00 00 26 40 00 00 00 00 00 00 36 40
        component	    size(起-止) decimal  hex
        SRID            4(0-3)     0        00 00 00 00
        Byte order	    1(4-4)     1        01
        WKB type	    4(5-8)     1        01 00 00 00
        point count     4(9-12)    3        03 00 00 00
        X(经度)          8(13-20)   1 	    00 00 00 00 00 00 f0 3f
        Y(纬度)          8(21-28)   2 	    00 00 00 00 00 00 00 40
        X(经度)          8(29-36)   15 	    00 00 00 00 00 00 2e 40
        Y(纬度)          8(37-44)   15       00 00 00 00 00 00 2e 40
        X(经度)          8(45-52)   11       00 00 00 00 00 00 2e 40
        Y(纬度)          8(53-60)   22       00 00 00 00 00 00 2e 40
   */
    /**
     * 解析Line
     * @param bytes bytes
     * @return Line
     */
    public static Line parseLine(byte[] bytes){
        boolean bigEndian = (bytes[4] == 0x00);
        int count = NumberUtil.byte2int(bytes, 9, 4, bigEndian);
        List<Point> points = new ArrayList<>();
        for(int i=0; i<count; i++){
            double x = NumberUtil.byte2double(bytes, 13+8*i*2);
            double y = NumberUtil.byte2double(bytes, 21+8*i*2);
            Point point = new Point(x, y);
            points.add(point);
        }
        Line line = new Line(points);
        return line;
    }
    /*

        头部（Header）：
            SRID
            字节顺序（Byte Order）：表示二进制数据的字节顺序，通常为大端序（Big Endian）或小端序（Little Endian）。
            类型标识符（Type Identifier）：标识几何对象的类型，对于多边形（Polygon）来说，它的值是十六进制的0103。
            环的数量（Number of Rings）：表示多边形中环的数量，包括外部环和内部环（孔）。
        外部环（Exterior Ring）：
            点的数量（Number of Points）：表示构成外部环的点的数量。
            点的坐标（Coordinates）：按照顺序列出外部环中每个点的坐标，每个点的坐标由X和Y值组成。
        内部环（Interior Rings）（可选）：
            环的数量（Number of Rings）：表示内部环的数量。
            点的数量（Number of Points）：表示每个内部环中点的数量。
            点的坐标（Coordinates）：按照顺序列出每个内部环中每个点的坐标，每个点的坐标由X和Y值组成。
        单个环
        POLYGON((121.415703 31.172893,121.415805 31.172664,121.416127 31.172751,121.41603 31.172976,121.415703 31.172893)
        bytes[97]:
        00 00 00 00, 01, 03 00 00 00, 01 00 00 00, 05 00 00 00, 57 76 C1 E0 9A 5A 5E 40, 13 B5 34 B7 42 2C 3F 40, DA 20 93 8C 9C 5A 5E 40, 51 32 39 B5 33 2C 3F 40, E3 FE 23 D3 A1 5A 5E 40, EF 59 D7 68 39 2C 3F 40, EA 09 4B 3C A0 5A 5E 40, 2E FE B6 27 48 2C 3F 40, 57 76 C1 E0 9A 5A 5E 40, 13 B5 34 B7 42 2C 3F 40
        component        size(起-止) decimal      hex
        SRID            4(0-3)       0            00 00 00 00
        Byte order      1(4-4)     1            01
        WKB type        4(5-8)       3            03 00 00 00
        rings count     4(9-12)      1            01 00 00 00
        外部环(注意这里的外部环只能有一个，如果有多个就是MultiPolygon了)
        points count    4(13-16)     5            05 00 00 00
        X(经度)          8(17-24)    121.415703   57 76 C1 E0 9A 5A 5E 40
        Y(纬度)          8(25-32)    31.172893    13 B5 34 B7 42 2C 3F 40
        X(经度)          8(33-40)    121.415805   DA 20 93 8C 9C 5A 5E 40
        Y(纬度)          8(41-48)    31.172664    51 32 39 B5 33 2C 3F 40
        X(经度)          8(49-56)    121.416127   E3 FE 23 D3 A1 5A 5E 40
        Y(纬度)          8(57-64)    31.172751    EF 59 D7 68 39 2C 3F 40
        X(经度)          8(65-72)    121.41603    EA 09 4B 3C A0 5A 5E 40
        Y(纬度)          8(73-80)    31.172976    2E FE B6 27 48 2C 3F 40
        X(经度)          8(81-88)    121.415703   57 76 C1 E0 9A 5A 5E 40
        Y(纬度)          8(89-96)    31.172893    13 B5 34 B7 42 2C 3F 40
*/
    /*
        多个环(含内环)
        POLYGON ((30 20, 45 40, 10 40, 30 20), (20 30, 35 35, 30 20, 20 30), (25 25, 30 35, 15 30, 25 25))
        bytes[217]
        00 00 00 00, 01, 03 00 00 00, 03 00 00 00,
        04 00 00 00, 00 00 00 00 00 00 3E 40, 00 00 00 00 00 00 34 40, 00 00 00 00 00 80 46 40, 00 00 00 00 00 00 44 40, 00 00 00 00 00 00 24 40, 00 00 00 00 00 00 44 40, 00 00 00 00 00 00 3E 40, 00 00 00 00 00 00 34 40,
        04 00 00 00, 00 00 00 00 00 00 34 40, 00 00 00 00 00 00 3E 40, 00 00 00 00 00 80 41 40, 00 00 00 00 00 80 41 40, 00 00 00 00 00 00 3E 40, 00 00 00 00 00 00 34 40, 00 00 00 00 00 00 34 40, 00 00 00 00 00 00 3E 40,
        04 00 00 00, 00 00 00 00 00 00 39 40, 00 00 00 00 00 00 39 40, 00 00 00 00 00 00 3E 40, 00 00 00 00 00 80 41 40, 00 00 00 00 00 00 2E 40, 00 00 00 00 00 00 3E 40, 00 00 00 00 00 00 39 40 ,00 00 00 00 00 00 39 40
        component        size(起-止)   decimal      hex
        SRID             4(0-3)       0            00 00 00 00
        Byte order       1(4-4)       1            01
        WKB type         4(5-8)       1            03 00 00 00
        rings count      4(9-12)      3            03 00 00 00
        外环(注意这里的外环只能有一个，如果有多个就是MultiPolygon了)
        外环points数量    4(13-16)      4            04 00 00 00
        X(经度)          8(17-24)     30            00 00 00 00 00 00 3E 40
        Y(纬度)          8(25-32)     20            00 00 00 00 00 00 34 40
        X(经度)          8(33-40)     45            00 00 00 00 00 80 46 40
        Y(纬度)          8(41-48)     40            00 00 00 00 00 00 44 40
        X(经度)          8(49-56)     10            00 00 00 00 00 00 24 40
        Y(纬度)          8(57-64)     40            00 00 00 00 00 00 44 40
        X(经度)          8(65-72)     30            00 00 00 00 00 00 3E 40
        Y(纬度)          8(73-80)     20            00 00 00 00 00 00 34 40
        内环
        points count    4(81-84)      4            04 00 00 00
        X(经度)          8(85-92)     20            00 00 00 00 00 00 34 40
        Y(纬度)          8(93-100)    30            00 00 00 00 00 00 3E 40
        X(经度)          8(101-108)   35            00 00 00 00 00 80 41 40
        Y(纬度)          8(109-116)   35            00 00 00 00 00 80 41 40
        X(经度)          8(117-124)   30            00 00 00 00 00 00 3E 40
        Y(纬度)          8(125-132)   20            00 00 00 00 00 00 34 40
        X(经度)          8(133-140)   20            00 00 00 00 00 00 34 40
        Y(纬度)          8(141-148)   30            00 00 00 00 00 00 3E 40
        points count    4(149-152)    4            04 00 00 00
        X(经度)          8(153-160)   25            00 00 00 00 00 00 39 40
        Y(纬度)          8(161-168)   25            00 00 00 00 00 00 39 40
        X(经度)          8(169-176)   30            00 00 00 00 00 00 3E 40
        Y(纬度)          8(177-184)   35            00 00 00 00 00 80 41 40
        X(经度)          8(185-192)   15            00 00 00 00 00 00 2E 40
        Y(纬度)          8(193-200)   30            00 00 00 00 00 00 3E 40
        X(经度)          8(201-208)   25            00 00 00 00 00 00 39 40
        X(经度)          8(209-216)   25            00 00 00 00 00 00 39 40

   */
    /**
     * 解析Polygon
     * @param bytes bytes
     * @return Polygon
     */
    public static Polygon parsePolygon(byte[] bytes){
        boolean bigEndian = (bytes[4] == 0x00);
        Polygon polygon = new Polygon();
        //环数量
        int index = 9;
        int ring_count = NumberUtil.byte2int(bytes, index, 4, bigEndian);
        index+=4;
        //外环(只有一个)
        //外环中Point数量
        int point_count = NumberUtil.byte2int(bytes, index, 4, bigEndian);
        index+=4;
        List<Point> points = new ArrayList<>();
        for(int p=0; p<point_count; p++){
            double x = NumberUtil.byte2double(bytes, index);
            index+=8;
            double y = NumberUtil.byte2double(bytes, index);
            index+=8;
            Point point = new Point(x, y);
            points.add(point);
        }
        Ring out = new Ring(points);
        out.clockwise(true);
        polygon.add(out);
        if(ring_count > 1){
            //内环(可能有多个)
            for(int r=1; r<ring_count; r++){
                //内环中Point数量
                points = new ArrayList<>();
                point_count = NumberUtil.byte2int(bytes, index, 4, bigEndian);
                index+=4;
                for(int p=0; p<point_count; p++){
                    double x = NumberUtil.byte2double(bytes, index);
                    index+=8;
                    double y = NumberUtil.byte2double(bytes, index);
                    index+=8;
                    Point point = new Point(x, y);
                    points.add(point);
                }
                Ring in = new Ring(points);
                in.clockwise(false);
                polygon.add(in);
            }
        }
        return polygon;
    }
}
