package org.anyline.simple.clear.controller;

import org.anyline.controller.impl.AnylineController;

/**
 * 这里一般要创建一个全局的base controller作为其他controller的父类
 * BasicController可以继承自AnylineController
 * AnylineController里已经注入了AnylineService
 */
public class BasicController extends AnylineController {
}
