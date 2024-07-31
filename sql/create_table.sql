create table public.flood
(
    id         bigint  default nextval('flood_id_seq'::regclass) not null
        primary key,
    gid        integer,
    surface_el numeric,
    still_wate numeric(38, 2),
    total_wate double precision,
    u_velocity numeric,
    v_velocity numeric,
    current_sp numeric,
    current_di numeric,
    element_ar numeric,
    geom       geometry,
    table_id   varchar(255),
    geom_ycl   varchar(255),
    sid        numeric default 1
);

alter table public.flood
    owner to postgres;

create index idx_flood_geom
    on public.flood using gist (geom);

create index idx_flood_geom_spgist
    on public.flood using spgist (geom);

