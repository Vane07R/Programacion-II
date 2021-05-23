package com.ugb.miprimercalculadora;

public class MyFirebaseInstanceIdService extends FirebaseMessagingService {
    public String obtenerToken(){
        AtomicReference<String> token = null;
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            if( !task.isSuccessful() ){
                return;
            }
            token.set(task.getResult());
        });
        return token.get();
    }
}