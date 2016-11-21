# Savewithme TODO

* Create User
* Login User
* Social Auth

#### Savewithme Entities

# User
* id
* firstnames
* lastname
* email
* password
* confirmed
* avatar
* last_login
* created_date
* update_date

# userToken
* id
* user_id
* token
* expired
* expiry_date
* created_date
* update_date

# savingsBucket
* id
* total_members
* start_date
* end_date
* total_cycle
* member_contribution_amount
* status
* bucket_admin
* created_date
* update_date

# usersSavingsBucket
* id
* save_bucket_id
* user_id
* created_date
* update_date

# savingsBucketContribution
* id
* save_bucket_id
* user_id
* contributed_amount
* cycle_sequence
* mode_of_payment - direct deposit to beneficiary account, card charge
* created_date
* update_date

# userCashOut
* id
* save_bucket_id
* user_id
* cash_out_amount
* cycle_sequence - position of guy cashing out in the bucket list
* cash_out_date
* payout_status - User account credit
* admin_fee ***
* created_date
* update_date

# userAccount
* id
* user_id
* total_cash_outs
* total_contributions
* status
* created_date
* update_date
