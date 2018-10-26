## Build a playground app using Sync API with Contentstack’s Android SDK

This is a demo playground app built using Contentstack’s Android SDK and Sync API. You can try out and play with our Sync API with this example app, before building bigger and better applications. 

Perform the steps given below to use this app.

### Prerequisites
* [Android Studio](https://developer.android.com/studio/)
* [Contentstack account](https://app.contentstack.com/#!/login)
* [Basic knowledge of Contentstack](https://www.contentstack.com/docs/)

In this tutorial, we will first go through the steps involved in configuring Contentstack and then look at the steps required to customize and use the presentation layer.

### Step 1: Create a stack
Log in to your Contentstack account, and [create a new stack](https://www.contentstack.com/docs/guide/stack#create-a-new-stack). Read more about [stacks](https://www.contentstack.com/docs/guide/stack).

### Step 2: Add a publishing environment
[Add a publishing environment](https://www.contentstack.com/docs/guide/environments#add-an-environment
) to publish your content in Contentstack. Provide the necessary details as per your requirement. Read more about [environments](https://www.contentstack.com/docs/guide/environments).

### Step 3: Import content types
For this app, we need just one content type: Session. Here’s what it’s needed for:
* Session: Lets you add the session content to your app

For quick integration, we have already created the content type. [Download the content type](https://drive.google.com/open?id=1jnpNIHRb4kNcP3r0I2QMatGzPwNXxpzS
) and [import](https://www.contentstack.com/docs/guide/content-types#importing-a-content-type) it to your stack. (If needed, you can [create your own content types](https://www.contentstack.com/docs/guide/content-types#creating-a-content-type). Read more about Content Types.)

Now that all the content types are ready, let’s add some content for your sync playground app.

### Step 4: Adding content
[Create](https://www.contentstack.com/docs/guide/content-management#add-a-new-entry) and [publish](https://www.contentstack.com/docs/guide/content-management#publish-an-entry) entries for the ‘Session’ content type

Now that we have created the sample data, it’s time to use and configure the presentation layer.

### Step 5: Set up and initialize Android SDK
To set up and initialize Contentstack’s Android SDK, refer to our documentation [here](https://www.contentstack.com/docs/platforms/android#getting-started).

### Step 6: Clone and configure the application
To get your app up and running quickly, we have created a sample playground app. Clone the Github repo given below and change the configuration as per your need:

$ git clone [https://github.com/contentstack/contentstack-android-sync-playground.git](https://github.com/contentstack/contentstack-android-sync-playground.git)

Now add your Contentstack API Key, Delivery Token, and Environment to the project during the SDK initialization step. (Find your [Stack's API Key and Delivery Token](https://www.contentstack.com/docs/apis/content-delivery-api/#authentication).)

```
Stack stack = Contentstack.stack(context, “api_key”, “delivery_token”, “environment”); 
```

This will initiate your project.

### Step 7: Initialize sync
To perform initial sync, use the sync method, which fetches all the content of the specified environment. 

```
stack.sync(new SyncResultCallBack() {
   @Override
   public void onCompletion(SyncStack syncStack, Error error) {
       if (error == null) {
           // Success block
               int itemSize = syncStack.getCount();
               int items = syncStack.getItems().size();
               String syncToken = syncStack.getSyncToken();
       }else{
           // Error block
           Log.e("Error", error.getErrorMessage());
       }
   }}); 
   ```
   On successful sync completion, you will get a sync token in response, which you need to use to get subsequent (delta) syncs.
   
   Screenshot
   
   <img src="https://github.com/contentstack/contentstack-android-sync-playground/blob/master/app/src/main/assets/screenshot/initial.png"  height="500" width="300">
   
### Step 8: Use pagination token
   
If the result of the initial sync contains more than 100 records, the response would be paginated. In that case, it returns a pagination token. While the SDK continues to automatically fetch the next batch of data using the pagination token, it comes in handy in case the sync process is interrupted midway (due to network issues, etc.). You can use it to reinitiate sync from where it was interrupted.
   
   ```
   // Call this function when initial sync is interrupted while 
   // paginating and you receive the Pagination Token
   stack.syncWithPaginationToken("blt3f33333d333333fba333e3", new SyncResultCallBack() {
   @Override
   public void onCompletion(SyncStack syncStack, Error error) {
       if (error == null){
               // Success block
               int itemSize = syncStack.getCount();
               int items = syncStack.getItems().size();
               String syncToken = syncStack.getSyncToken();
       }else{
         // Error block
         Log.e("Error", error.getErrorMessage());
       }
   }});
   ```
   
   ### Step 9: Publish new entries
   In order to understand how you can also fetch only new (incremental) updates that were done since the last sync, you should create more entries and publish them. You can then use the Subsequent Sync call given below to see how it works.
   
   ### Step 10: Perform subsequent sync
   In the response of the initial sync, you get a sync token in the response. This token is used to fetch incremental updates, i.e., only the changes made after the initial sync. Use the syncToken method to perform subsequent syncs. 
   ```
   // Dummy token blt3d033333333a3daa333c3
   stack.syncToken("blt3d033333333a3daa333c3", new SyncResultCallBack() {
    @Override
    public void onCompletion(SyncStack syncStack, Error error) {
        if (error == null) {
            // Success block
                int itemSize = syncStack.getCount();
                int items = syncStack.getItems().size();
                String syncToken = syncStack.getSyncToken();
        }else{
            // Error block
            Log.e("Error", error.getErrorMessage());
        }
    }});
```

Screenshot
   
   <img src="https://github.com/contentstack/contentstack-android-sync-playground/blob/master/app/src/main/assets/screenshot/subsquesnt.png"  height="500" width="300">



### More Resources
* [Getting started with Android SDK](https://www.contentstack.com/docs/platforms/android)
* [Using the Sync API with Android SDK](https://www.contentstack.com/docs/guide/synchronization/using-the-sync-api-with-android-sdk)
* [Sync API documentation](https://www.contentstack.com/docs/apis/content-delivery-api/#synchronization) 



