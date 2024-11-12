import 'package:flutter/material.dart';

class DataAttachments {
  List<String> listAttachment;
  int initPosition;
  String contentSuccess;
  String contentFail;

  DataAttachments(
      {required this.listAttachment,
      required this.initPosition,
      this.contentSuccess = "Tải tệp thành công",
      this.contentFail =
          "Tải tệp thất bại, vui lòng kiểm tra lại đường dẫn"});
}