package saedc.example.com.View.Account;

import saedc.example.com.Model.Entity.Account;

public interface AccountRecyclerItemClickListener {
    void onItemClick(Account clickedAccount);

    void onItemLongClick(int longClickedAccountId);
}
