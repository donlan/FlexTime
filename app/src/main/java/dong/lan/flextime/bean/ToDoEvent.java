package dong.lan.flextime.bean;

/**
 * Created by 梁桂栋 on 2015/12/12.
 */
public class ToDoEvent {
    private ToDo toDo;
    private int type;
    private int pos;

    public ToDoEvent(ToDo toDo,int type,int pos)
    {
        this.toDo =toDo;
        this.type =type;
        this.pos = pos;
    }

    public int getPos() {
        return pos;
    }

    public ToDo getToDo()
    {
        return toDo;
    }
    public int getType()
    {
        return type;
    }

}
