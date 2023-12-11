package vit.projects.hudeem.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vit.projects.hudeem.dto.RecordDTO;
import vit.projects.hudeem.entities.GoalEntity;
import vit.projects.hudeem.entities.RecordEntity;
import vit.projects.hudeem.mappers.RecordMapper;
import vit.projects.hudeem.repositories.GoalRepository;
import vit.projects.hudeem.repositories.RecordRepository;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RecordService {
    private final RecordRepository recordRepository;
    private final GoalRepository goalRepository;
    private final RecordMapper recordMapper;
    private final MetricService metricService;
    private final RecordValidationService recordValidationService;

    public String saveRecord(RecordDTO recordDTO) {
        RecordEntity recordEntity = recordMapper.fromDTO(recordDTO);
        recordValidationService.validate(recordEntity);
        recordRepository.save(recordEntity);
        updateMetricsAndSave(recordEntity.getGoal());
        return "successful";
    }

    private RecordEntity getLatestRecord(GoalEntity goalEntity) {
        return goalEntity.getRecords()
                .stream()
                .max(Comparator.comparing(RecordEntity::getDate))
                .get();
    }

    public void deleteRecordsByGoalId(Long goalId) {
        List<RecordEntity> recordEntities = recordRepository.findAllByGoalId(goalId).get();
        recordRepository.deleteAll(recordEntities);
    }

    public String deleteRecord(long id) {
        GoalEntity goalEntity = recordRepository.findById(id).get().getGoal();
        recordRepository.deleteById(id);
        updateMetricsAndSave(goalEntity);
        return "Запись успешно удалена!";
    }

    private void updateMetricsAndSave(GoalEntity goalEntity) {
        RecordEntity latestRecord = getLatestRecord(goalEntity);
        GoalEntity updatedGoalEntity = metricService.getUpdatedWithAllMetrics(latestRecord);
        goalRepository.save(updatedGoalEntity);
    }
}