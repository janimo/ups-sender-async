
# ups-sender-async

An asynchronous HTTP library for the Ubuntu Push Server.

This is heavily based on Open Whisper System's gcm-sender-async library.

## Installing

Add to your pom:

```
<dependency>
  <groupId>ro.startx</groupId>
  <artifactId>ups-sender-async</artifactId>
  <version>(latest version here)</version>
</dependency>
```

## Using

```
Sender sender = new Sender();

ListenableFuture<Result> future = sender.send(Message.newBuilder()
                                                     .withToken("<unique token")
                                                     .withAppId("appname.user_appname")
                                                     .withExpireOn("2016-12-24T03:00:00.000Z") //date in ISO 8601 format
                                                     .withDataPart("message", "hello world!");

Futures.addCallback(future, new FutureCallback<Result>() {
  @Override
  public void onSuccess(Result result) {
    if (result.isSuccess()) {
      // Maybe do something with result.getMessageId()
    } else {
      // Maybe do something with result.getError(), or check result.isUnregistered, etc..
    }
  }

  @Override
  public void onFailure(Throwable throwable) {
    // Handle network failure or server 500
  }
}
```

License
---------------------

Copyright 2015 Open Whisper Systems

Licensed under the AGPLv3: https://www.gnu.org/licenses/agpl-3.0.html
