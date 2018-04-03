 protected void paymentResponseVerification(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        //Create this servlet to revieve the ajax request shown in the javascript file

        HttpClient client = HttpClientBuilder.create().build();
        try
        {

            HttpGet newRequest = new HttpGet("https://api.paystack.co/transaction/verify/" + paymentRef);
            newRequest.addHeader("Content-type", "application/json");
            newRequest.addHeader("Authorization", "Bearer " + PayStackData.MY_PAY_STACK_SECRET_KEY);
            newRequest.addHeader("Cache-Control", "no-cache");
            HttpResponse newResponse = client.execute(newRequest);
            HttpEntity entity = newResponse.getEntity();
            StringBuffer jb = new StringBuffer();
            String line = null;
            if(entity != null)
                try
                {
                    BufferedReader rd = new BufferedReader(new InputStreamReader(entity.getContent()));
                    while((line = rd.readLine()) != null)
                        jb.append(line);
                }
                catch(Exception e)
                {
                    throw new RuntimeException(e);
                }
            else
                throw new Exception("Error Occured while connecting to paystack url");

            Gson g = new Gson(); //Instantiating the gson class
            VerifyPayStackTransaction payRes = g.fromJson(jb.toString(), VerifyPayStackTransaction.class);
            if(payRes == null || payRes.getStatus().equals("false"))
                throw new Exception("An error occurred while verifying payment");
            else if(payRes.getData().getStatus().equals("success"))//Check if payment was successful
                try
                {
                    boolean validateTrans = DonationDAO.donationVerification(email, transactionId);
                    boolean insertTransDetails = TransactionDAO.insertTransactionDetails(email, donCategory, amount, transactionId, paymentRef);
                    if(validateTrans == false || insertTransDetails == false)
                        throw new Exception("There was an error somewhere");
                    else
                       //Transaction was successfull if you got here.....so have fun !!!
                }
                catch(Exception xcp)
                {
                    throw new RuntimeException(xcp);
                }
        }
        catch(Exception e)
        {
            throw new RuntimeException(e);
        }
    }
