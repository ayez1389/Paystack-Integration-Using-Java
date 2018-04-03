<script>
            $(function(){
                PaystackPop.setup({
                    key: 'public_key_goes_here', //Your public API Key
                    reference: 'TO090000090909', //Transaction refrence key
                    email: 'example@example.com', //Email adddress of the user 
                    amount: 1000, //Amount to be paid in Kobo
                    container: 'paystackEmbedContainer',
                    callback: function(response){ // Paystack response after payment
                        $.ajax({
                            url: "paystack_response_servlet", // Create a servlet to recieve this request
                            data: {paymentResponse: response.reference, regEmail: email, cash: amount, transId: transId},
                            type: 'GET',
                            contentType: "application/json",
                            cache: false,
                            beforeSend: function(xhr){
                                alert('Payment was successfull. transaction ref is ' + response.reference);
                                //response.reference is paystack auto generated transation ID
                            },
                            success: function(content){
  //Add custom event 
                            },
                            error: function(){
                                alert('There was an error please contact us stating your transaction information');
                            }
                        });
                    },
                    onClose: function(){
                        alert('window closed');
                    },
                    error: function(){
                        alert('There was an error please reload this page.');
                    }
                });
            });
        </script>
