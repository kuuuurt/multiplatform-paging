//
//  ViewController.swift
//  Sample
//
//  Created by Kurt on 05/03/2020.
//  Copyright © 2020 paging.kuuurt.com. All rights reserved.
//

import UIKit
import MultiplatformPagingLibrary

class ViewController: UIViewController, UITableViewDelegate, UITableViewDataSource {
  @IBOutlet weak var testTableView: UITableView!
  
  private let viewModel = MainViewModel.init()
  
  private var tests: [String] = []
  private var count: Int = 0
  
  override func viewDidLoad() {
    super.viewDidLoad()
    testTableView.delegate = self
    testTableView.dataSource = self
  
    testTableView.register(
      UINib.init(nibName: "TestTableViewCell", bundle: nil),
      forCellReuseIdentifier: "Test"
    )
    
    viewModel.paginator.pagedList.watch { [unowned self] nullableArray in
      guard let list = nullableArray?.compactMap({ $0 as? String }) else {
        return
      }
      
      self.tests = list
      self.testTableView.reloadData()
    }
    
    viewModel.paginator.totalCount.watch { [unowned self] nullable in
      guard let testCount = nullable as? Int else {
        return
      }
      self.count = testCount
      self.testTableView.reloadData()
    }
  }


  func numberOfSections(in tableView: UITableView) -> Int {
    1
  }
  
  func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
    count
  }
  
  func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
    let cell = testTableView.dequeueReusableCell(
      withIdentifier: "Test",
      for: indexPath
    ) as! TestTableViewCell
    
    var text = "Loading..."
    if (indexPath.row < tests.count) {
      text = tests[indexPath.row]
    }
    cell.txtTest.text = text
    
    return cell
  }
  
  
  func tableView(_ tableView: UITableView, willDisplay cell: UITableViewCell, forRowAt indexPath: IndexPath) {
    let cell = cell as! TestTableViewCell
    
    if (cell.txtTest.text == "Loading...") {
      viewModel.paginator.loadMore()
    }
  }
}

