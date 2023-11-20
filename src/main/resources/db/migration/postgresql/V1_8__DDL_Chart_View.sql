CREATE OR REPLACE VIEW public.view_chart
AS
SELECT c.clinic_id,
       pt.patient_tracker_id,
       p.patient_id,
       p.mrn,
       p.first_name,
       p.last_name,
       ( SELECT string_agg(treatment_meta_lookup.meta_value::text, ','::text) AS cancer_sites
         FROM treatment_meta_lookup
                  LEFT JOIN patient_treatment_info pti ON pti.patient_tracker_id = pt.patient_tracker_id
         WHERE treatment_meta_lookup.treatment_meta_lookup_id = pti.treatment_site_id
       ) AS cancer_sites,
       ( SELECT json_agg(json_build_object('name', clinic_users.name)) AS treatment_teams
         FROM clinic_users
                  LEFT JOIN treatment_teams tt ON tt.patient_tracker_id = pt.patient_tracker_id
         WHERE clinic_users.clinic_user_id = tt.clinic_user_id) AS treatment_teams,
       ( SELECT json_agg(json_build_object('stage_type', tsl.stage_type, 'spread_type', tsl.spread_type, 'stage', tsl.stage)) AS staging
         FROM tnm_stages_lookup tsl
                  LEFT JOIN patient_treatment_stages pts ON pts.patient_tracker_id = pt.patient_tracker_id
         WHERE tsl.tnm_stage_lookup_id = pts.tnm_stage_lookup_id) AS staging,
       ( SELECT json_agg(json_build_object('structure', ptg.structure, 'structure_measure', ptg.structure_measure, 'primary_goal_dose', ptg.primary_goal_dose, 'primary_goal_dose_unit', ptg.primary_goal_dose_unit, 'primary_goal_dose_comparator', ptg.primary_goal_dose_comparator, 'primary_goal_dose_limit', ptg.primary_goal_dose_limit, 'primary_goal_dose_limit_unit', ptg.primary_goal_dose_limit_unit, 'variation_dose', ptg.variation_dose, 'variation_dose_unit', ptg.variation_dose_unit, 'variation_dose_comparator', ptg.variation_dose_comparator, 'variation_dose_limit', ptg.variation_dose_limit, 'variation_dose_limit_unit', ptg.variation_dose_limit_unit, 'priority', ptg.priority)) AS organ_at_risk
         FROM patient_treatment_goals ptg
         WHERE ptg.patient_tracker_id = pt.patient_tracker_id) AS organ_at_risk,
       pos.brief_plan,
       ( SELECT json_agg(json_build_object('total_fractions', pp.total_fractions, 'total_doses', pp.total_dose_in_cgy)) AS prescription
         FROM patient_prescriptions pp
                  LEFT JOIN patient_treatment_info pti ON pti.patient_tracker_id = pt.patient_tracker_id
         WHERE pp.patient_treatment_info_id = pti.patient_treatment_info_id) AS prescription,
       ( SELECT string_agg(pti.treatment_start_date::text, ','::text) AS start_date
         FROM patient_treatment_info pti
         WHERE pti.patient_tracker_id = pt.patient_tracker_id) AS start_date,
       pc.note,
       wf.name AS workflow
FROM patient_trackers pt
         LEFT JOIN patients p ON p.patient_id = pt.patient_id
         LEFT JOIN clinics c ON c.clinic_id = p.clinic_id
         LEFT JOIN patient_oncology_summary pos ON pos.patient_oncology_summary_id = pt.patient_tracker_id
         LEFT JOIN clinic_users cu ON cu.clinic_user_id = p.clinic_user_id
         LEFT JOIN patient_charts pc ON pc.patient_chart_id = pt.patient_tracker_id
         LEFT JOIN patient_tracker_steps pts on pts.patient_tracker_id = pt.patient_tracker_id
         LEFT JOIN workflow_steps ws ON ws.workflow_step_id = pts.workflow_step_id
         LEFT JOIN workflows wf on wf.workflow_id = ws.workflow_id
WHERE ws.step::text = 'TREATMENT_PLANNING'::text;
