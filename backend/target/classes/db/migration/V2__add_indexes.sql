CREATE INDEX idx_transactions_user_date ON transactions(user_id, date DESC);
CREATE INDEX idx_transactions_user_cat ON transactions(user_id, category);
CREATE INDEX idx_transactions_user_type ON transactions(user_id, type);

CREATE INDEX idx_budgets_user_month_year ON budgets(user_id, month, year);
