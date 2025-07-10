-- Update inventory table to add threshold column
USE drinks_distributor;

-- Add threshold column to inventory table
ALTER TABLE inventory ADD COLUMN threshold INT NOT NULL DEFAULT 5;

-- Update existing records to set threshold to reorder_level
UPDATE inventory SET threshold = reorder_level;

COMMIT; 