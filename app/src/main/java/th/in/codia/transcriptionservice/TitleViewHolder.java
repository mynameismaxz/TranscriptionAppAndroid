package th.in.codia.transcriptionservice;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by mynameismaxz on 7/15/15 AD.
 */
public class TitleViewHolder extends RecyclerView.ViewHolder {
    protected TextView Title;
    protected TextView User;
    protected TextView Date;
    protected CardView Card;

    public TitleViewHolder(View itemView) {
        super(itemView);
        Title = (TextView)itemView.findViewById(R.id.title);
        User = (TextView)itemView.findViewById(R.id.user);
        Date = (TextView)itemView.findViewById(R.id.date);
        Card = (CardView)itemView;
    }
}
