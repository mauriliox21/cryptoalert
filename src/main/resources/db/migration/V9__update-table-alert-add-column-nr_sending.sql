ALTER TABLE IF EXISTS public.tb_alert
    ADD COLUMN nr_sending integer;

UPDATE TB_ALERT SET NR_SENDING = 0 WHERE NR_SENDING IS NULL;