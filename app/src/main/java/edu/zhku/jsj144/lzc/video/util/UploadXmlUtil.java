package edu.zhku.jsj144.lzc.video.util;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;
import edu.zhku.jsj144.lzc.video.application.BaseApplication;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.*;

public class UploadXmlUtil {

    public static class Video {
        private String vid;
        private String path;
        private int progress;

        public String getVid() {
            return vid;
        }

        public void setVid(String vid) {
            this.vid = vid;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public int getProgress() {
            return progress;
        }

        public void setProgress(int progress) {
            this.progress = progress;
        }
    }

    private static void checkDirExists(Context context) throws IOException {
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/vi/uploading");
        if (!file.exists()) {
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdir();
            }
            file.mkdir();
        }
    }

    public static void addUploadingVideo(Context context, String vid, String path) {
        /*
         * 1. 得到Document
         * 2. 得到root元素
         * 3. 要把Video对象转换成Element元素
         * 4. 把video元素插入到root元素中
         * 5. 回写document
         */
        try {
            checkDirExists(context);
            /*
             * 1. 得到Docuembnt
             */
            // 创建解析器
            SAXReader reader = new SAXReader();
            // 调用读方法，获取或新建Document和Root Element
            Document doc = null;
            Element root = null;

            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                    + "/vi/uploading/" + SharedPreferencesUtil.getString(context, "uid", ""));
            if (file.exists()) {
                doc = reader.read(file);
                if ((root = doc.getRootElement()) == null) {
                    root = doc.addElement("uVideo");
                }
            } else {
                doc = DocumentHelper.createDocument();
                /*
                 * 2. 加入根元素
                 */
                root = doc.addElement("uVideo");
            }


            /*
             * 3. 完成添加元素，并返回添加的元素！
             * 向root中添加一个名为video的元素！并返回这个元素
             */
            Element video = root.addElement("video");
            // 设置videoElement的属性！
            video.addAttribute("vid", vid);
            video.addAttribute("path", path);
            video.addAttribute("progress", String.valueOf(0));
            video.addAttribute("isUploading", "0");

            /*
             * 回写
             * 注意：创建的xml需要使用工具修改成UTF-8编码！
             * Editplus：标记列--> 重新载入为 --> UTF-8
             */

            // 创建目标输出流，它需要与xml文件绑定
            Writer out = new PrintWriter(Environment.getExternalStorageDirectory().getAbsolutePath()
                    + "/vi/uploading/" + SharedPreferencesUtil.getString(context, "uid", ""),
                    "UTF-8");
            // 创建格式化器
            OutputFormat format = new OutputFormat("\t", true);
            format.setTrimText(true);//先干掉原来的空白(\t和换行和空格)！

            // 创建XMLWrtier
            XMLWriter writer = new XMLWriter(out, format);

            // 调用它的写方法，把document对象写到out流中。
            writer.write(doc);

            // 关闭流
            out.close();
            writer.close();
        } catch(Exception e) {
            e.printStackTrace();
            Toast.makeText(BaseApplication.getContext(),"下载状态存取异常", Toast.LENGTH_LONG).show();
        }
    }

    public static Video getUploadingVideoInfo(Context context, String vid) {
        /*
         * 1. 得到Docuemnt
         * 2. 给出xpath表达式
         * 3. 调用docuemnt的方法进行xpath查询，得到Element
         * 4. 把Element转换成Video对象，返回！
         */
        Video video = new Video();
        try {
            /*
             * 1. 得到Docuembnt
             */
            // 创建解析器
            SAXReader reader = new SAXReader();
            // 调用读方法，得到Document
            Document doc = reader.read(new File(Environment.getExternalStorageDirectory()
                    .getAbsolutePath() + "/vi/uploading/"
                    + SharedPreferencesUtil.getString(context, "uid", "")));

            /*
             * 2. 准备xpath
             *  //开头表示没有深度限制，可以在文档查询子元素、子元素的子元素！
             *  []中放的叫谓语，其实就是查询条件
             *  @vid表示vid属性，限定其必须等于方法参数vid
             */
            String xpath = "//video[@vid='" + vid + "']";
            /*
             * 3. 调用document方法完成查询
             */
            Element videoEle = (Element)doc.selectSingleNode(xpath);
            if(videoEle == null) {
                return null;
            }
            /*
             * 4. 把元素转换成Video类的对象，然后返回
             */
            // 获取元素的vid属性值，赋给对象的vid属性
            video.setVid(videoEle.attributeValue("vid"));
            video.setPath(videoEle.attributeValue("path"));
            video.setProgress(Integer.parseInt(videoEle.attributeValue("progress")));
        } catch(Exception e) {
            e.printStackTrace();
//            Toast.makeText(BaseApplication.getContext(),"下载状态存取异常", Toast.LENGTH_LONG).show();
        }
        return video;
    }

    public static String getCurrentUploadingVideoID(Context context) {
        Element videoEle = null;
        try {
            /*
             * 1. 得到Docuembnt
             */
            // 创建解析器
            SAXReader reader = new SAXReader();
            // 调用读方法，得到Document
            Document doc = reader.read(new File(Environment.getExternalStorageDirectory()
                    .getAbsolutePath() + "/vi/uploading/"
                    + SharedPreferencesUtil.getString(context, "uid", "")));

            /*
             * 2. 准备xpath
             *  //开头表示没有深度限制，可以在文档查询子元素、子元素的子元素！
             *  []中放的叫谓语，其实就是查询条件
             *  @vid表示vid属性，限定其必须等于方法参数vid
             */
            String xpath = "//video[@isUploading='1']";
            /*
             * 3. 调用document方法完成查询
             */
            videoEle = (Element)doc.selectSingleNode(xpath);
        } catch(Exception e) {
            e.printStackTrace();
//            Toast.makeText(BaseApplication.getContext(),"下载状态存取异常", Toast.LENGTH_LONG).show();
        }
        if (videoEle == null) {
            return "0";
        }
        return videoEle.attributeValue("vid");
    }

    public static void setIsUploading(Context context, String vid, int isUploading) {
        try {
            /*
             * 1. 得到Docuembnt
             */
            // 创建解析器
            SAXReader reader = new SAXReader();
            // 调用读方法，得到Document
            Document doc = reader.read(new File(Environment.getExternalStorageDirectory()
                    .getAbsolutePath() + "/vi/uploading/"
                    + SharedPreferencesUtil.getString(context, "uid", "")));

            /*
             * 2. 准备xpath
             *  //开头表示没有深度限制，可以在文档查询子元素、子元素的子元素！
             *  []中放的叫谓语，其实就是查询条件
             *  @vid表示vid属性，限定其必须等于方法参数vid
             */
            String xpath = "//video[@vid='" + vid + "']";
            /*
             * 3. 调用document方法完成查询
             */
            Element videoEle = (Element)doc.selectSingleNode(xpath);
            if(videoEle == null) {
                return;
            }

            /*
             * 4. 修改属性值
             */
            videoEle.attribute("isUploading").setValue(String.valueOf(isUploading));

            /*
             * 5. 回写
             * 注意：创建的xml需要使用工具修改成UTF-8编码！
             * Editplus：标记列--> 重新载入为 --> UTF-8
             */

            // 创建目标输出流，它需要与xml文件绑定
            Writer out = new PrintWriter(Environment.getExternalStorageDirectory().getAbsolutePath()
                    + "/vi/uploading/" + SharedPreferencesUtil.getString(context, "uid", ""),
                    "UTF-8");
            // 创建格式化器
            OutputFormat format = new OutputFormat("\t", true);
            format.setTrimText(true);//先干掉原来的空白(\t和换行和空格)！

            // 创建XMLWrtier
            XMLWriter writer = new XMLWriter(out, format);

            // 调用它的写方法，把document对象写到out流中。
            writer.write(doc);

            // 关闭流
            out.close();
            writer.close();
        } catch(Exception e) {
            e.printStackTrace();
            Toast.makeText(BaseApplication.getContext(),"下载状态存取异常", Toast.LENGTH_LONG).show();
        }
    }

    public static void setProgress(Context context, String vid, int progress) {
        try {
            /*
             * 1. 得到Docuembnt
             */
            // 创建解析器
            SAXReader reader = new SAXReader();
            // 调用读方法，得到Document
            Document doc = reader.read(new File(Environment.getExternalStorageDirectory()
                    .getAbsolutePath() + "/vi/uploading/"
                    + SharedPreferencesUtil.getString(context, "uid", "")));

            /*
             * 2. 准备xpath
             *  //开头表示没有深度限制，可以在文档查询子元素、子元素的子元素！
             *  []中放的叫谓语，其实就是查询条件
             *  @vid表示vid属性，限定其必须等于方法参数vid
             */
            String xpath = "//video[@vid='" + vid + "']";
            /*
             * 3. 调用document方法完成查询
             */
            Element videoEle = (Element)doc.selectSingleNode(xpath);
            if(videoEle == null) {
                return;
            }

            /*
             * 4. 修改属性值
             */
            videoEle.attribute("progress").setValue(String.valueOf(progress));

            /*
             * 5. 回写
             * 注意：创建的xml需要使用工具修改成UTF-8编码！
             * Editplus：标记列--> 重新载入为 --> UTF-8
             */

            // 创建目标输出流，它需要与xml文件绑定
            Writer out = new PrintWriter(Environment.getExternalStorageDirectory().getAbsolutePath()
                    + "/vi/uploading/" + SharedPreferencesUtil.getString(context, "uid", ""),
                    "UTF-8");
            // 创建格式化器
            OutputFormat format = new OutputFormat("\t", true);
            format.setTrimText(true);//先干掉原来的空白(\t和换行和空格)！

            // 创建XMLWrtier
            XMLWriter writer = new XMLWriter(out, format);

            // 调用它的写方法，把document对象写到out流中。
            writer.write(doc);

            // 关闭流
            out.close();
            writer.close();
        } catch(Exception e) {
            e.printStackTrace();
            Toast.makeText(BaseApplication.getContext(),"下载状态存取异常", Toast.LENGTH_LONG).show();
        }
    }

    public static void removeUploadingVideo(Context context, String vid) {
        try {
            /*
             * 1. 得到Docuembnt
             */
            // 创建解析器
            SAXReader reader = new SAXReader();
            // 调用读方法，得到Document
            Document doc = reader.read(new File(Environment.getExternalStorageDirectory()
                    .getAbsolutePath() + "/vi/uploading/"
                    + SharedPreferencesUtil.getString(context, "uid", "")));

            /*
             * 2. 准备xpath
             *  //开头表示没有深度限制，可以在文档查询子元素、子元素的子元素！
             *  []中放的叫谓语，其实就是查询条件
             *  @vid表示vid属性，限定其必须等于方法参数vid
             */
            String xpath = "//video[@vid='" + vid + "']";
            /*
             * 3. 调用document方法完成查询
             */
            Element videoEle = (Element)doc.selectSingleNode(xpath);
            if(videoEle == null) {
                return;
            }

            /*
             * 4. 移除节点
             */
            videoEle.detach();

            /*
             * 5. 回写
             * 注意：创建的xml需要使用工具修改成UTF-8编码！
             * Editplus：标记列--> 重新载入为 --> UTF-8
             */

            // 创建目标输出流，它需要与xml文件绑定
            Writer out = new PrintWriter(Environment.getExternalStorageDirectory().getAbsolutePath()
                    + "/vi/uploading/" + SharedPreferencesUtil.getString(context, "uid", ""),
                    "UTF-8");
            // 创建格式化器
            OutputFormat format = new OutputFormat("\t", true);
            format.setTrimText(true);//先干掉原来的空白(\t和换行和空格)！

            // 创建XMLWrtier
            XMLWriter writer = new XMLWriter(out, format);

            // 调用它的写方法，把document对象写到out流中。
            writer.write(doc);

            // 关闭流
            out.close();
            writer.close();
        } catch(Exception e) {
            e.printStackTrace();
            Toast.makeText(BaseApplication.getContext(),"下载状态存取异常", Toast.LENGTH_LONG).show();
        }
    }
}
