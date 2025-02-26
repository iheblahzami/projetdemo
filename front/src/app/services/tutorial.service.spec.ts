import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { TutorialService } from './tutorial.service';
import { Tutorial } from '../models/tutorial.model';

describe('TutorialService', () => {
  let service: TutorialService;
  let httpMock: HttpTestingController;
  const baseUrl = 'http://localhost:8080/api/tutorials';

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [TutorialService]
    });
    service = TestBed.inject(TutorialService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should retrieve all tutorials', () => {
    const mockTutorials: Tutorial[] = [
      { id: 1, title: 'Test1', description: 'Desc1', published: true },
      { id: 2, title: 'Test2', description: 'Desc2', published: false }
    ];

    service.getAll().subscribe(tutorials => {
      expect(tutorials.length).toBe(2);
      expect(tutorials).toEqual(mockTutorials);
    });

    const req = httpMock.expectOne(baseUrl);
    expect(req.request.method).toBe('GET');
    req.flush(mockTutorials);
  });

  it('should get a tutorial by ID', () => {
    const mockTutorial: Tutorial = { id: 1, title: 'Test1', description: 'Desc1', published: true };

    service.get(1).subscribe(tutorial => {
      expect(tutorial).toEqual(mockTutorial);
    });

    const req = httpMock.expectOne(`${baseUrl}/1`);
    expect(req.request.method).toBe('GET');
    req.flush(mockTutorial);
  });

  it('should create a tutorial', () => {
    const newTutorial = { title: 'New', description: 'New Desc' };

    service.create(newTutorial).subscribe(response => {
      expect(response).toEqual(newTutorial);
    });

    const req = httpMock.expectOne(baseUrl);
    expect(req.request.method).toBe('POST');
    req.flush(newTutorial);
  });

});
