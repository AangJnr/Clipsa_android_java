package com.clipsa.utilities;

import com.cowtribe.cowtribeapps.data.db.entity.Order;
import com.cowtribe.cowtribeapps.data.network.model.SearchResponse;

public class Callbacks {

    public interface SyncCompleteListener {
        void onSuccess(String message);
        void onError(Throwable throwable);
    }



    public interface SearchCompleteListener {
        void onSuccess(SearchResponse response);
        void onError(Throwable throwable);
    }


    public interface CompletePaymentListener{
        void onCompletePaymentClicked(String amountPaid, boolean hasPaidInFull, double balance, String paymentMethod);
    }

    public interface ImageCaptureListener{
        void onImageCaptureComplete(String base64);
    }

    public interface UssdListener{
        void onUssdPaymentSuccess(String transactionId);
    }

    public interface PrintListener{
        void onPrintSuccess();
        void onError(Throwable throwable);    }


    public interface OnEventListener<T>{
        void onEventSuccess(T object);
        void onError(Throwable throwable);
    }

    public interface ScrollToPosition{
        void scrollTo(int position);
     }

    public interface BounceAnimationListener{
        void onAnimated(Order order);
    }

    public interface ShowSnackBarListener{
        void showUndoSnackBar();
    }



    public interface FragmentCallback {

        void onFragmentAttached();

        void onFragmentDetached(String tag);
    }

}
