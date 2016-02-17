package innovator.memories.models;

/**
 * Created by SachendraSingh on 2/16/16.
 */
public class Update {

    enum UpdateType {
        Users,
        Groups,
        GroupMembers
    }

    public UpdateType type;
    public Object data;

    public Update(UpdateType type, Object data){
        this.type = type;
        this.data = data;
    }

}
