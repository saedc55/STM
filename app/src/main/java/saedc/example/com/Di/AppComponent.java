package saedc.example.com.Di;

import saedc.example.com.LoginPage.LoginViewModel;
import saedc.example.com.View.Profile.ProfileViewModel;
import saedc.example.com.View.AddAndEditSaving.AddAndEditSavingViewModel;
import saedc.example.com.View.AddAndEditSpending.AddAndEditSpendingViewModel;
import saedc.example.com.View.ChartList.chartViewModel;
import saedc.example.com.View.SavingList.SavingListViewModle;
import saedc.example.com.View.SpendingList.SpendingListViewModel;
import saedc.example.com.View.Summary.SummaryViewModel;
import saedc.example.com.View.TotalSpendingPrice.TotalSpendingPriceViewModel;

import javax.inject.Singleton;

import dagger.Component;


@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {

    void inject(AddAndEditSavingViewModel addAndEditSavingViewModel);

    void inject(SavingListViewModle savingListViewModle);

    void inject(chartViewModel chartviewModel);

    void inject(SpendingListViewModel spendingListViewModel);

    void inject(TotalSpendingPriceViewModel totalSpendingPriceViewModel);

    void inject(AddAndEditSpendingViewModel addSpendingViewModel);

    void inject(SummaryViewModel summaryViewModel);

    void inject(LoginViewModel loginViewModel);

    void inject(ProfileViewModel profileViewModel);
}
