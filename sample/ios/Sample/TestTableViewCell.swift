//
//  TestTableViewCell.swift
//  Sample
//
//  Created by Kurt on 06/03/2020.
//  Copyright © 2020 paging.kuuurt.com. All rights reserved.
//

import UIKit

class TestTableViewCell: UITableViewCell {

  @IBOutlet weak var txtTest: UILabel!

  override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
    
}
