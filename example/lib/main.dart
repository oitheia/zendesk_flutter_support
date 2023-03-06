import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:zendesk_flutter_combination/zendesk_flutter_combination.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatefulWidget {
  const MyApp({Key? key}) : super(key: key);

  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String _platformVersion = 'Unknown';
  final _zendeskFlutterCombinationPlugin = ZendeskFlutterCombination();
  final List<String> channelMessages = [];

  @override
  void initState() {
    super.initState();
    initPlatformState();

    ZendeskFlutterCombination.setMessageHandler((type, arguments) {
      setState(() {
        channelMessages.add("$type - args=$arguments");
      });
    });
  }

  // Platform messages are asynchronous, so we initialize in an async method.
  Future<void> initPlatformState() async {
    String platformVersion;
    // Platform messages may fail, so we use a try/catch PlatformException.
    // We also handle the message potentially returning null.
    try {
      platformVersion =
          await _zendeskFlutterCombinationPlugin.getPlatformVersion() ?? 'Unknown platform version';
    } on PlatformException {
      platformVersion = 'Failed to get platform version.';
    }

    // If the widget was removed from the tree while the asynchronous platform
    // message was in flight, we want to discard the reply rather than calling
    // setState to update our non-existent appearance.
    if (!mounted) return;

    setState(() {
      _platformVersion = platformVersion;
    });
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Center(
          child: Column(children: [Text('Running on: $_platformVersion\n'),
            ElevatedButton(
              style: ElevatedButton.styleFrom(
                shape: RoundedRectangleBorder(
                  borderRadius: BorderRadius.circular(6), // <-- Radius
                ),
                elevation: 0,
                primary: Colors.pink,
                minimumSize: const Size.fromHeight(
                    40), // fromHeight use double.infinity as width and 40 is the height
              ),
              onPressed: () {
                ZendeskFlutterCombination.initialize(
                   appId: "{zendeskApplicationId}",
                   clientId: "{zendeskMobileClientId}",
                   userIdentity: "{customerNameIdentifieer}",
                   urlString: "{yourCompanyzendeskDomain}");
              },
              child: Text(
                'Initialize',

              ),
            ),
            SizedBox(height: 20,),
            ElevatedButton(
              style: ElevatedButton.styleFrom(
                shape: RoundedRectangleBorder(
                  borderRadius: BorderRadius.circular(6), // <-- Radius
                ),
                elevation: 0,
                primary: Colors.pink,
                minimumSize: const Size.fromHeight(
                    40), // fromHeight use double.infinity as width and 40 is the height
              ),
              onPressed: () {
                ZendeskFlutterCombination.showHelpCenter();
              },
              child: Text(
                'Help Center',

              ),
            ),
            SizedBox(height: 20,),
            ElevatedButton(
              style: ElevatedButton.styleFrom(
                shape: RoundedRectangleBorder(
                  borderRadius: BorderRadius.circular(6), // <-- Radius
                ),
                elevation: 0,
                primary: Colors.pink,
                minimumSize: const Size.fromHeight(
                    40), // fromHeight use double.infinity as width and 40 is the height
              ),
              onPressed: () {
                ZendeskFlutterCombination.showRequestList();
              },
              child: Text(
                'Request List',

              ),
            ),
            SizedBox(height: 20,),
            ElevatedButton(
              style: ElevatedButton.styleFrom(
                shape: RoundedRectangleBorder(
                  borderRadius: BorderRadius.circular(6), // <-- Radius
                ),
                elevation: 0,
                primary: Colors.pink,
                minimumSize: const Size.fromHeight(
                    40), // fromHeight use double.infinity as width and 40 is the height
              ),
              onPressed: () {
                ZendeskFlutterCombination.showRequest();
              },
              child: Text(
                'Request',

              ),
            ),
          ],)
        ),
      ),
    );
  }
}
