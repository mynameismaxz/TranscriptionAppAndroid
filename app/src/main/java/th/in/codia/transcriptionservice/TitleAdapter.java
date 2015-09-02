package th.in.codia.transcriptionservice;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Puri on 7/15/15 AD.
 */
public class TitleAdapter extends RecyclerView.Adapter<TitleViewHolder> {

    private List<MenuTitle> MenuTitle;

    public TitleAdapter(List<MenuTitle> Menu){
        this.MenuTitle = new ArrayList<MenuTitle>();
        this.MenuTitle.addAll(Menu);
    }

    @Override
    public TitleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.cardtitle, parent, false);
        return new TitleViewHolder(item);
    }

    @Override
    public void onBindViewHolder(TitleViewHolder holder, int position) {
        MenuTitle Menu = this.MenuTitle.get(position);
        holder.Title.setText(Menu.getTitle());
        holder.User.setText(Menu.getUser());
        holder.Date.setText(Menu.getStartT()+" -> "+Menu.getStopT()+" : "+Menu.getDate());
    }

    @Override
    public int getItemCount() {
        return MenuTitle.size();
    }
}
