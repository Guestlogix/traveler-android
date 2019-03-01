package com.guestlogix.traveleruikit.viewmodels;

import androidx.lifecycle.ViewModel;
import com.guestlogix.travelercorekit.models.Product;

public abstract class ProductViewModel extends ViewModel {

    public abstract void setup(Product product);
}
