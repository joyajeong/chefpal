package au.edu.unsw.infs3634.unswgamifiedlearningapp;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

public class CategoryAdapter extends BaseAdapter {

    private List <CategoryQuiz> categoryList;

    public CategoryAdapter(List<CategoryQuiz> categoryList) {
        this.categoryList = categoryList;
    }

    @Override
    public int getCount() {
        return categoryList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        View mView;
        if(view == null){
            mView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cat_item_layout,viewGroup,false);
        }
        else{
            mView = view;
        }
        mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(),TestActivity.class);
                //pass position
                intent.putExtra("CAT_INDEX",i);
                view.getContext().startActivity(intent);
            }
        });

        TextView catName = mView.findViewById(R.id.catName);
        TextView testNo = mView.findViewById(R.id.testNo);
        catName.setText(categoryList.get(i).getName());
        testNo.setText(String.valueOf(categoryList.get(i).getNumberOfTests()) + " Tests");


        return mView;
    }
}
