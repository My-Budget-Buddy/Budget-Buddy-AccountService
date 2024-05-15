
-- This script is not used, it is more for referencing purposes only

-- Table: public.accounts

-- DROP TABLE IF EXISTS public.accounts;

CREATE TABLE IF NOT EXISTS public.accounts
(
    id integer NOT NULL DEFAULT nextval('accounts_id_seq'::regclass),
    account_number character varying(255) COLLATE pg_catalog."default",
    institution character varying(255) COLLATE pg_catalog."default",
    investment_rate numeric(38,2),
    routing_number character varying(255) COLLATE pg_catalog."default",
    starting_balance numeric(38,2),
    _type character varying(255) COLLATE pg_catalog."default",
    user_id character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT accounts_pkey PRIMARY KEY (id),
    CONSTRAINT accounts__type_check CHECK (_type::text = ANY (ARRAY['CHECKING'::character varying, 'SAVINGS'::character varying, 'CREDIT'::character varying, 'INVESTMENT'::character varying]::text[]))
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.accounts
    OWNER to postgres;
