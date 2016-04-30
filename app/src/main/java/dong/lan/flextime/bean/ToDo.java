package dong.lan.flextime.bean;

import android.support.annotation.NonNull;
import android.util.SparseArray;

/**
 * 项目：FlexTime
 * 作者：梁桂栋
 * 日期： 3/26/2016  11:09.
 */
public class Todo implements Comparable<Todo> {

    private String createTime;  //创建时间
    private int type; //日程类型
    private SparseArray<ToDoItem> todos; //分步日程
    private User user; //创建的用户
    private String id; //唯一的ID值
    private int flag;  //日程的标记位(待做，超时，完成)
    private double weight; //日程的排序权重

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }
    public Todo(){}

    public Todo(String createTime, int type, SparseArray<ToDoItem> todos, User user, String id, int flag, double weight) {
        this.createTime = createTime;
        this.type = type;
        this.todos = todos;
        this.user = user;
        this.id = id;
        this.flag = flag;
        this.weight = weight;
    }

    public Todo(String createTime, int type, double weight, SparseArray<ToDoItem> todos, String id, User user) {
        this.createTime = createTime;
        this.type = type;
        this.todos = todos;
        this.id = id;
        this.weight = weight;
        this.user = user;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public SparseArray<ToDoItem> getTodos() {
        return todos;
    }

    public void setTodos(SparseArray<ToDoItem> todos) {
        this.todos = todos;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void addTodoItem(ToDoItem item)
    {
        todos.put(todos.size(),item);
    }

    @Override
    public int compareTo(@NonNull Todo another) {
        if(this.getWeight()>another.getWeight())
        return -1;
        else if(this.getWeight()==another.getWeight())
            return 0;
        return 1;
    }
}
