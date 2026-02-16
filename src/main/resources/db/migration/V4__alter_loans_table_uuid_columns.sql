-- Migration to fix loans table to match LoanEntity
-- V1 created a basic loans table, V3's CREATE TABLE IF NOT EXISTS was skipped
-- This migration adds the missing columns and converts types to UUID

-- Drop the existing foreign key constraint from V1
ALTER TABLE loans DROP CONSTRAINT IF EXISTS fk_copy;

-- Add missing columns that V3 tried to create but were skipped
ALTER TABLE loans ADD COLUMN IF NOT EXISTS loan_id UUID DEFAULT gen_random_uuid() UNIQUE;
ALTER TABLE loans ADD COLUMN IF NOT EXISTS user_id UUID;

-- Convert copy_id from INT to UUID
ALTER TABLE loans ALTER COLUMN copy_id TYPE UUID USING gen_random_uuid();

-- Set NOT NULL constraints
ALTER TABLE loans ALTER COLUMN loan_id SET NOT NULL;
ALTER TABLE loans ALTER COLUMN user_id SET NOT NULL;
ALTER TABLE loans ALTER COLUMN copy_id SET NOT NULL;

-- Change expected_returned_date from TIMESTAMP to DATE
ALTER TABLE loans ALTER COLUMN expected_returned_date TYPE DATE;

-- Ensure users.user_id has NOT NULL constraint
ALTER TABLE users ALTER COLUMN user_id SET NOT NULL;

